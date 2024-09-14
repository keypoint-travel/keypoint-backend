package com.keypoint.keypointtravel.oauth.service.apple;

import com.keypoint.keypointtravel.global.constants.AppleAPIConstants;
import com.keypoint.keypointtravel.oauth.dto.useCase.appleTokenUseCase.AppleTokenResponseUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appleApiClient", url = AppleAPIConstants.AUTH_URL, fallback = AppleAPIServiceFallback.class)
public interface AppleAPIService {

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE,
        headers = "Content-Type=application/x-www-form-urlencoded")
    AppleTokenResponseUseCase getValidateToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("code") String code,
        @RequestParam("grant_type") String grantType
    );
}
