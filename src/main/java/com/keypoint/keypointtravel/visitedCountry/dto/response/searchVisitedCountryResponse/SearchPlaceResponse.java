package com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchPlaceResponse {

    private Long placeId;
    private double latitude;
    private double longitude;
    private String markerImageUrl;

    public void setMarkerImageUrl(String markerImageUrl) {
        this.markerImageUrl = markerImageUrl;
    }
}
