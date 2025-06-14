package com.keypoint.keypointtravel.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.config.security.attribute.OAuthAttributes;
import com.keypoint.keypointtravel.global.config.security.session.SessionUser;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.GeneralOAuth2AuthenticationException;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private static final String APPLE_REGISTRATION_ID = "apple";
    private static final String ID_TOKEN = "id_token";
    private static final RoleType ROLE_USER = RoleType.ROLE_CERTIFIED_USER;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Value("${spring.security.oauth2.authorizedRedirectUri}")
    private String redirectUri;

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
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User;
        if (registrationId.contains(APPLE_REGISTRATION_ID)) {
            oAuth2User = loadAppleUser(userRequest);
        } else {
            oAuth2User = delegate.loadUser(userRequest);
        }

        try {
            return process(userRequest, oAuth2User);
        } catch (IOException e) {
            throw new GeneralOAuth2AuthenticationException();
        }
    }

    private OAuth2User loadAppleUser(OAuth2UserRequest userRequest) {
        String idToken = userRequest.getAdditionalParameters().get(ID_TOKEN).toString();
        Map<String, Object> attributes;

        try {
            attributes = decodeJwtTokenPayload(idToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to decode JWT token payload", e);
        }

        attributes.put(ID_TOKEN, idToken);

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("resultcode", "00");
        userAttributes.put("message", "success");
        userAttributes.put("response", attributes);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(ROLE_USER.toString())),
            userAttributes,
            "response"
        );
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User)
        throws IOException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
            registrationId,
            userNameAttributeName,
            oAuth2User.getAttributes()
        );
        CommonMemberDTO member = saveOrUpdate(attributes, registrationId);
        httpSession.setAttribute("member", SessionUser.from(member));
        return CustomUserDetails.of(member, oAuth2User.getAttributes());
    }

    private CommonMemberDTO saveOrUpdate(
        OAuthAttributes attributes,
        String registrationId
    )
        throws GeneralOAuth2AuthenticationException {
        OauthProviderType oauthProviderType = attributes.getOAuthProvider();
        String email = attributes.getEmail();

        // 1. 이메일이 등록되어 있는지 확인
        return registerMember(email, "임시", oauthProviderType);
    }

    public CommonMemberDTO registerMember(
        String email,
        String name,
        OauthProviderType oauthProviderType
    ) {
        // 1. 이메일이 등록되어 있는지 확인
        Optional<CommonMemberDTO> memberOptional = memberRepository.findByEmailAndIsDeletedFalse(email);

        // 2. 등록되지 않은 경우: 저장 / 다른 제공사로 등록되어 있는 경우 예외 발생
        if (memberOptional.isPresent()) {
            // 2-1. 로그인 (이전에 등록되어 있는 이메일)
            CommonMemberDTO member = memberOptional.get();

            if (member.getRole() == RoleType.ROLE_UNCERTIFIED_USER
                && member.getOauthProviderType() != oauthProviderType) {
                // 2-1-1. 현재 등록한 Oauth 로 변경 (Oauth type 의 변경된 경우)
                if (name == null) {
                    name = member.getName();
                    LogUtils.writeWarnLog("registerMember", "Name receives null value");
                }

                memberRepository.updateOauthProviderTypeByMemberId(
                    member.getId(),
                    name,
                    oauthProviderType
                );
            } else {
                // 2-1-2. 유효성 검사
                validateOauthProvider(member, oauthProviderType);
            }

            return member;
        } else {
            // 2-2. 회원가입: 임시 회원으로 등록 (등록되어 있지 않은 이메일)
            if (name == null) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "간편 로그인의 첫 번쨰 시도인 경우, name이 null이면 안됩니다.");
            }

            return addUser(email, name, oauthProviderType);
        }
    }

    private void validateOauthProvider(CommonMemberDTO member, OauthProviderType oauthProviderType)
        throws GeneralOAuth2AuthenticationException {
        if (member.getOauthProviderType() != oauthProviderType) {
            throw new GeneralOAuth2AuthenticationException(
                MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER
            );
        }
    }

    private CommonMemberDTO addUser(
        String email,
        String name,
        OauthProviderType oauthProviderType
    ) {
        Member newMember = Member.of(email, name, oauthProviderType);
        memberRepository.save(newMember);

        // 초대코드 저장: 초대코드가 중복되지 않도록 문자열에 마지막에 memberId를 추가하여 저장
        newMember.setInvitationCode(StringUtils.getRandomString(9) + newMember.getId());

        return memberRepository.findByEmailAndIsDeletedFalse(newMember.getEmail()).get();
    }

    /**
     * Oauth의 redirect url을 반환하는 함수
     *
     * @param request
     * @return
     */
    public String getOauthRedirectURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("Host") + redirectUri;
    }
}
