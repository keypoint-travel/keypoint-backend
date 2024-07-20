package com.keypoint.keypointtravel.oauth.service;

public interface OauthService {

    /**
     * 소셜 로그인 토큰 재발급
     */
    void reissue();


    /**
     * 소셜 로그인 연동해제
     */
    void unlink();

}
