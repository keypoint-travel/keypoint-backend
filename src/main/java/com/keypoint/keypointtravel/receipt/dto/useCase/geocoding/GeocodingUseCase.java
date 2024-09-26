package com.keypoint.keypointtravel.receipt.dto.useCase.geocoding;

import com.keypoint.keypointtravel.global.enumType.api.MapResultStatus;
import lombok.Getter;

@Getter
public class GeocodingUseCase {

    private MapResultStatus status;
    private GeocodingResultUseCase results;
}
