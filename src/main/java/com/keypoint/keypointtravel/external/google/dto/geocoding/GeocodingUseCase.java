package com.keypoint.keypointtravel.external.google.dto.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.global.enumType.api.MapResultStatus;
import java.util.List;
import lombok.Getter;

@Getter
public class GeocodingUseCase {

    private MapResultStatus status;

    private List<GeocodingResultUseCase> results;

    @JsonProperty("error_message")
    private String errorMessage;
}
