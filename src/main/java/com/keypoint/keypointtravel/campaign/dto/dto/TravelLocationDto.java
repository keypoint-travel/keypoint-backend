package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelLocationDto {

    private final Long sequence;
    private final Long placeId;
    private final String cityName;
    private final String countryName;
}
