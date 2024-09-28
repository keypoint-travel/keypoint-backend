package com.keypoint.keypointtravel.external.google.dto.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class GeocodingResultUseCase {

    @JsonProperty("address_components")
    private List<Object> addressComponents;

    @JsonProperty("formatted_address")
    private String formattedAddress;

    private GeometryUseCase geometry;

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("plus_code")
    private Object plusCode;

    private List<String> types;
}
