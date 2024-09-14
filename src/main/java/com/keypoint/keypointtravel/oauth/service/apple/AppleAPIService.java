package com.keypoint.keypointtravel.oauth.service.apple;

import com.keypoint.keypointtravel.global.constants.AppleAPIConstants;
import com.keypoint.keypointtravel.oauth.dto.useCase.appleTokenUseCase.AppleTokenRequestUseCase;
import com.keypoint.keypointtravel.oauth.dto.useCase.appleTokenUseCase.AppleTokenResponseUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "appleApiClient", url = AppleAPIConstants.AUTH_URL, fallback = AppleAPIServiceFallback.class)
public interface AppleAPIService {

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE,
        consumes = "application/x-www-form-urlencoded")
    AppleTokenResponseUseCase getValidateToken(
        @RequestBody AppleTokenRequestUseCase request
    );
}
