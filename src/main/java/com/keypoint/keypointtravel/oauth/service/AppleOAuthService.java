package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.auth.redis.service.OAuthTokenService;
import com.keypoint.keypointtravel.oauth.dto.useCase.ReissueRefreshTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppleOAuthService implements OAuthService {

    private final OAuthTokenService oAuthTokenService;

    @Override
    public void reissue(Long memberId) {
        // 1. 토큰 재발급이 필요한지 확인
        ReissueRefreshTokenUseCase useCase = oAuthTokenService.checkIsNeedToReissueToken(memberId);
        if (useCase.isNeedToReissue()) {
            // 2. 토큰 재발급

            // 3. 토큰 업데이트
        }
    }

    @Override
    public void unlink() {

    }
}
