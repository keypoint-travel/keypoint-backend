package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.premium.dto.apple.AppleAppStoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static com.keypoint.keypointtravel.global.constants.ApplePurchaseConstants.APPLE_SANDBOX_URL;

@FeignClient(name = "appleApiTest", url = APPLE_SANDBOX_URL, fallback = ApplePurchaseApiServiceFallback.class)
public interface ApplePurchaseApiTestService {

    @PostMapping
    AppleAppStoreResponse verifyReceiptTest(@RequestBody Map<String, String> appStoreRequest);
}
