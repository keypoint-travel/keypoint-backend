package com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CountryDetailContentUseCase {

    private String name;
    private String iso2;

    @JsonProperty(value = "long")
    private Double longitude;

    @JsonProperty(value = "lat")
    private Double latitude;
}
