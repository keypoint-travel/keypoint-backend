package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.premium.dto.apple.AppleAppStoreResponse;
import java.util.Map;

public class ApplePurchaseApiServiceFallback implements ApplePurchaseApiService, ApplePurchaseApiTestService{

    @Override
    public AppleAppStoreResponse verifyReceipt(Map<String, String> appStoreRequest) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public AppleAppStoreResponse verifyReceiptTest(Map<String, String> appStoreRequest) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}
