package com.keypoint.keypointtravel.oauth.service;

public interface OAuthService {

    /**
     * 소셜 로그인 토큰 재발급
     */
    void reissue(Long memberId);


    /**
     * 소셜 로그인 연동해제
     */
    void unlink();

}
