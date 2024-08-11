package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.oauth.dto.useCase.GoogleUserInfoUseCase;
import org.springframework.stereotype.Component;

@Component
public class GoogleAPIServiceFallback implements GoogleAPIService {

    @Override
    public GoogleUserInfoUseCase getUserInfo(String authorization) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}