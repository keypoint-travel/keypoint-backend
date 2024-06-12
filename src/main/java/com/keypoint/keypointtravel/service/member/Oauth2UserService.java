package com.keypoint.keypointtravel.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.global.enumType.RoleType;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralOAuth2AuthenticationException;
import com.keypoint.keypointtravel.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.config.security.attribute.OAuthAttributes;
import com.keypoint.keypointtravel.config.security.session.SessionUser;
import com.keypoint.keypointtravel.entity.member.Member;
import com.keypoint.keypointtravel.repository.member.MemberRepository;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private static final String APPLE_REGISTRATION_ID = "apple";
    private static final String ID_TOKEN = "id_token";
    private static final RoleType ROLE_USER = RoleType.ROLE_UNCERTIFIED_USER;

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    public static Map<String, Object> decodeJwtTokenPayload(String jwtToken)
        throws JsonProcessingException {
        Map<String, Object> jwtClaims = new HashMap<>();

        String[] parts = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(decodedString, Map.class);
        jwtClaims.putAll(map);
        return jwtClaims;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User;
        if (registrationId.contains(APPLE_REGISTRATION_ID)) {
            String idToken = userRequest.getAdditionalParameters().get(ID_TOKEN).toString();
            Map<String, Object> attributes = null;
            try {
                attributes = decodeJwtTokenPayload(idToken);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            attributes.put(ID_TOKEN, idToken);
            Map<String, Object> userAttributes = new HashMap<>();
            userAttributes.put("resultcode", "00");
            userAttributes.put("message", "success");
            userAttributes.put("response", attributes);

            oAuth2User = new DefaultOAuth2User(
                Collections.singleton(
                    new SimpleGrantedAuthority(ROLE_USER.toString())),
                userAttributes,
                "response"
            );
        } else {
            oAuth2User = delegate.loadUser(userRequest);
        }

        try {
            return process(userRequest, oAuth2User);
        } catch (IOException e) {
            throw new GeneralOAuth2AuthenticationException();
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User)
        throws IOException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", SessionUser.of(member));
        return CustomUserDetails.toCustomUserDetails(member, oAuth2User.getAttributes());
    }

    public Member saveOrUpdate(OAuthAttributes attributes)
        throws GeneralOAuth2AuthenticationException {
        OauthProviderType oauthProviderType = attributes.getOAuthProvider();
        String email = attributes.getEmail();

        // 1. 이메일이 등록되어 있는지 확인
        List<Member> memberList = memberRepository.findByEmail(email);

        // 2. 등록되지 않은 경우: 저장 / 다른 제공사로 등록되어 있는 경우 예외 발생
        if (!memberList.isEmpty()) {
            Member member = memberList.get(0);
            validateOauthProvider(member, oauthProviderType);
            memberRepository.save(member);

            return member;
        } else {
            return addUser(attributes);
        }
    }

    private void validateOauthProvider(Member member, OauthProviderType oauthProviderType)
        throws GeneralOAuth2AuthenticationException {
        if (member.getOauthProviderType() != oauthProviderType) {
            throw new GeneralOAuth2AuthenticationException(
                MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER
            );
        }
    }

    private Member addUser(OAuthAttributes attributes) {
        Member newMember = attributes.toEntity();
        memberRepository.save(newMember);
        return newMember;
    }
}
