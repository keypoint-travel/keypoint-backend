package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.oauth.dto.response.OauthLoginResponse;
import com.keypoint.keypointtravel.oauth.dto.useCase.OauthLoginUseCase;

public interface OAuthService {

    /**
     * 소셜 로그인 토큰 재발급
     */
    OauthLoginResponse login(OauthLoginUseCase useCase);


    /**
     * 소셜 로그인 토큰 재발급
     */
    void reissue(Long memberId);


    /**
     * 소셜 로그인 연동해제
     */
    void unlink();

}
