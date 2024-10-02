package com.keypoint.keypointtravel.external.google.dto.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GeometryUseCase {

    private GeometryLocationUseCase location;

    @JsonProperty("location_type")
    private String locationType;

    private Object viewport;
}
