package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.global.constants.GoogleAPIConstants;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.oauth.dto.useCase.GoogleUserInfoUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleApiClient", url = GoogleAPIConstants.COMMON_URI, fallback = GoogleAPIServiceFallback.class)
public interface GoogleAPIService {

    @GetMapping(GoogleAPIConstants.FIND_USER_INFO)
    GoogleUserInfoUseCase getUserInfo(
        @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization);
}