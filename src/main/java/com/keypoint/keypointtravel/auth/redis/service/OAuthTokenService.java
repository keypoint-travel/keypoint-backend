package com.keypoint.keypointtravel.auth.redis.service;

import com.keypoint.keypointtravel.auth.redis.entity.OAuthToken;
import com.keypoint.keypointtravel.auth.redis.repository.OAuthTokenRepository;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthTokenService {

    private final OAuthTokenRepository oauthTokenRepository;

    /**
     * OAuthToken 저장하는 함수
     *
     * @param memberId 토큰을 발급받은 사용자
     * @param client   oauth 토큰 정보가 존재하는 객체
     */
    public void saveOAuthToken(Long memberId, OAuth2AuthorizedClient client) {
        if (client == null) {
            LogUtils.writeInfoLog("OAuthTokenService", "OAuth2AuthorizedClient : null");
            return;
        }

        // 1. 요청한 사용자에게 토큰 데이터가 존재하는지 확인
        Optional<OAuthToken> tokenOptional = oauthTokenRepository.findByMemberId(memberId);
        if (tokenOptional.isPresent()) {
            // 1-1. 저장된 토큰이 존재하는 경우 삭제
            oauthTokenRepository.delete(tokenOptional.get());
        }

        // 2. 저장
        OAuthToken oAuthToken = OAuthToken.of(memberId, client);
        oauthTokenRepository.save(oAuthToken);
    }

    /**
     * 토큰 재발급이 필요한지 확인하는 함수
     *
     * @param memberId
     * @return
     */
    public boolean checkIsNeedToReissueToken(Long memberId) {
        Optional<OAuthToken> oAuthTokenOptional = oauthTokenRepository.findByMemberId(memberId);
        if (oAuthTokenOptional.isPresent()) {
            OAuthToken oAuthToken = oAuthTokenOptional.get();
            return !LocalDateTime.now().isAfter(oAuthToken.getAccessTokenExpiredAt());
        } else {
            throw new GeneralException(HttpStatus.UNAUTHORIZED, TokenErrorCode.EXPIRED_TOKEN);
        }
    }

}
