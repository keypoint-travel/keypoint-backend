package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.premium.dto.apple.AppStoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static com.keypoint.keypointtravel.global.constants.ApplePurchaseConstants.APPLE_SANDBOX_URL;

@FeignClient(name = "appleApiTest", url = APPLE_SANDBOX_URL)
public interface ApplePurchaseApiTestService {

    @PostMapping
    AppStoreResponse verifyReceiptTest(@RequestBody Map<String, String> appStoreRequest);
}
