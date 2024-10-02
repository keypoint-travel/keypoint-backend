package com.keypoint.keypointtravel.oauth.service.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.redis.service.OAuthTokenService;
import com.keypoint.keypointtravel.global.constants.AppleAPIConstants;
import com.keypoint.keypointtravel.global.converter.Oauth2RequestEntityConverter;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.HttpUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.oauth.dto.request.ReissueGoogleRequest;
import com.keypoint.keypointtravel.oauth.dto.response.OauthLoginResponse;
import com.keypoint.keypointtravel.oauth.dto.response.ReissueOAuthResponse;
import com.keypoint.keypointtravel.oauth.dto.useCase.OauthLoginUseCase;
import com.keypoint.keypointtravel.oauth.dto.useCase.ReissueRefreshTokenUseCase;
import com.keypoint.keypointtravel.oauth.dto.useCase.appleToken.SuccessAppleTokenUseCase;
import com.keypoint.keypointtravel.oauth.service.OAuthService;
import com.keypoint.keypointtravel.oauth.service.Oauth2UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
public class AppleOAuthService implements OAuthService {

    private static final String GRANT_TYPE = "authorization_code";

    private final OAuthTokenService oAuthTokenService;
    private final AppleAPIService appleAPIService;
    private final Oauth2UserService oauth2UserService;
    private final JwtTokenProvider tokenProvider;
    private final Oauth2RequestEntityConverter oauth2RequestEntityConverter;

    @Value("${apple.keyPath}")
    private String appleKeyPath;
    @Value("${spring.security.oauth2.client.provider.apple.tokenUri}")
    private String tokenURL;
    @Value("${apple.app-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.apple.clientSecret}")
    private String clientSecret;
    @Value("${apple.teamId}")
    private String appleTeamId;
    @Value("${apple.keyId}")
    private String appleKeyId;

    @Override
    @Transactional
    public OauthLoginResponse login(OauthLoginUseCase useCase) {
        try {
            // 1. Oauth 토큰 발급
            SuccessAppleTokenUseCase tokenResponse = appleAPIService.getValidateToken(
                clientId,
                createSecret(),
                useCase.getOauthCode(),
                GRANT_TYPE
            );

            // 2. 사용자 정보 조회
            String email = getEmailFromIdToken(tokenResponse.getIdToken());

            // 3. 사용자 정보 저장
            CommonMemberDTO member = oauth2UserService.registerMember(
                email,
                useCase.getName(),
                OauthProviderType.APPLE
            );

            // 4. OAuth 토큰 저장
            oAuthTokenService.saveOAuthToken(
                member.getId(),
                tokenResponse.getAccessToken(),
                LocalDateTime.now().plusDays(7),
                useCase.getOauthRefreshToken(),
                null
            );

            // 5. JWT 토큰 발급
            Authentication authentication = tokenProvider.createAuthenticationFromMember(member);
            TokenInfoResponse token = tokenProvider.createToken(authentication);

            return OauthLoginResponse.of(
                member.getRole() == RoleType.ROLE_UNCERTIFIED_USER,
                token
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    private String getEmailFromIdToken(String idToken) {
        DecodedJWT jwt = JWT.decode(idToken);

        // 사용자 정보 추출
        String userId = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        boolean emailVerified = jwt.getClaim("email_verified").asBoolean();

        return email;
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
            ReissueOAuthResponse reissueResponse = response.getBody();

            // 3. 토큰 업데이트
            oAuthTokenService.saveOAuthToken(
                memberId,
                reissueResponse.getAccessToken(),
                reissueResponse.getExpiredAt(),
                reissueResponse.getRefreshToken(),
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

    public PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/key/" + appleKeyPath);

        try (InputStream in = resource.getInputStream();
            PEMParser pemParser = new PEMParser(
                new InputStreamReader(in, StandardCharsets.UTF_8))) {
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPrivateKey(object);
        }
    }

    public String createSecret() {
        Date expirationDate = Date.from(
            LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        try {
            return Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", appleKeyId)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience(AppleAPIConstants.COMMON_URL)
                .setSubject(clientId)
                .signWith(this.getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
