package com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchPlaceResponse {

    private Long placeId;
    private double latitude;
    private double longitude;
    private String markerImageUrl;
}
