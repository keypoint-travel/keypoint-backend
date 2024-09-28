package com.keypoint.keypointtravel.external.google.service;

import com.keypoint.keypointtravel.external.google.dto.geocoding.GeocodingUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.springframework.stereotype.Component;

@Component
public class GoogleMapAPIServiceFallback implements GoogleMapAPIService {

    @Override
    public GeocodingUseCase getGeocodingResult(String address, String key) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}
