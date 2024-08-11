package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.redis.service.OAuthTokenService;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.utils.HttpUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.oauth.dto.request.ReissueGoogleRequest;
import com.keypoint.keypointtravel.oauth.dto.response.OauthLoginResponse;
import com.keypoint.keypointtravel.oauth.dto.response.ReissueOAuthResponse;
import com.keypoint.keypointtravel.oauth.dto.useCase.GoogleUserInfoUseCase;
import com.keypoint.keypointtravel.oauth.dto.useCase.OauthLoginUseCase;
import com.keypoint.keypointtravel.oauth.dto.useCase.ReissueRefreshTokenUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final OAuthTokenService oAuthTokenService;
    private final GoogleAPIService googleAPIService;
    private final Oauth2UserService oauth2UserService;
    private final JwtTokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.provider.google.tokenUri}")
    private String tokenURL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Override
    @Transactional
    public OauthLoginResponse login(OauthLoginUseCase useCase) {
        String accessToken = useCase.getOauthAccessToken();

        // 1. 사용자 정보 요청
        String authorizationHeader = "Bearer " + accessToken;
        GoogleUserInfoUseCase userInfo = googleAPIService.getUserInfo(authorizationHeader);
        String email = userInfo.getEmail();

        // 2. 사용자 정보 저장
        CommonMemberDTO member = oauth2UserService.registerMember(email, OauthProviderType.GOOGLE);

        // 3. OAuth 토큰 저장
        oAuthTokenService.saveOAuthToken(
            member.getId(),
            accessToken,
            LocalDateTime.now().plusDays(7),
            useCase.getOauthRefreshToken(),
            null
        );

        // 4. JWT 토큰 발급
        Authentication authentication = tokenProvider.createAuthenticationFromMember(member);
        TokenInfoResponse token = tokenProvider.createToken(authentication);

        return OauthLoginResponse.of(
            member.getRole() == RoleType.ROLE_UNCERTIFIED_USER,
            token
        );
    }

    @Override
    public void reissue(Long memberId) {
        // 1. 토큰 재발급이 필요한지 확인
        ReissueRefreshTokenUseCase useCase = oAuthTokenService.checkIsNeedToReissueToken(memberId);
        if (true) {
            // 2. 토큰 재발급
            HttpHeaders headers = createHeader();
            ReissueGoogleRequest request = ReissueGoogleRequest.of(
                clientId,
                clientSecret,
                useCase.getRefreshToken()
            );
            // 2-1. 토큰 재발급 데이터 생성
            MultiValueMap<String, String> queryParams = request.toMap();

            String requestURL = UriComponentsBuilder.fromHttpUrl(tokenURL)
                .queryParams(queryParams)
                .toUriString();

            // 2-2. 요청
            ResponseEntity<ReissueOAuthResponse> response = HttpUtils.post(
                requestURL,
                headers,
                null,
                ReissueOAuthResponse.class);
            ReissueOAuthResponse reissueGoogleResponse = response.getBody();

            // 3. 토큰 업데이트
            oAuthTokenService.saveOAuthToken(
                memberId,
                reissueGoogleResponse.getAccessToken(),
                reissueGoogleResponse.getExpiredAt(),
                useCase.getRefreshToken(),
                null
            );
        }
    }

    @Override
    public void unlink() {

    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
