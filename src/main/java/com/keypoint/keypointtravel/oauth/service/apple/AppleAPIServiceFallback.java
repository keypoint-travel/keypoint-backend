package com.keypoint.keypointtravel.oauth.service.apple;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.oauth.dto.useCase.appleToken.SuccessAppleTokenUseCase;
import org.springframework.stereotype.Component;

@Component
public class AppleAPIServiceFallback implements AppleAPIService {


    @Override
    public SuccessAppleTokenUseCase getValidateToken(String clientId, String clientSecret,
        String code, String grantType) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}
