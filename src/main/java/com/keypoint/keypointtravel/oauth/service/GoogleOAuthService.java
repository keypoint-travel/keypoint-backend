package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.auth.redis.service.OAuthTokenService;
import com.keypoint.keypointtravel.global.utils.HttpUtils;
import com.keypoint.keypointtravel.oauth.dto.request.ReissueGoogleRequest;
import com.keypoint.keypointtravel.oauth.dto.response.ReissueGoogleResponse;
import com.keypoint.keypointtravel.oauth.dto.useCase.ReissueRefreshTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final OAuthTokenService oAuthTokenService;

    @Value("${spring.security.oauth2.client.provider.google.tokenUri}")
    private String tokenURL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

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
            ResponseEntity<ReissueGoogleResponse> response = HttpUtils.post(
                requestURL,
                headers,
                null,
                ReissueGoogleResponse.class);
            ReissueGoogleResponse reissueGoogleResponse = response.getBody();

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
