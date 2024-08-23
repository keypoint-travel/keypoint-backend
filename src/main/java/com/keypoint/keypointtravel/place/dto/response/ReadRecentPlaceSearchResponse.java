package com.keypoint.keypointtravel.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadRecentPlaceSearchResponse {

    private String placeSearchHistoryId;
    private Long placeId;

    public static ReadRecentPlaceSearchResponse of(
        String placeSearchHistoryId, Long placeId
    ) {
        return new ReadRecentPlaceSearchResponse(placeSearchHistoryId, placeId);
    }
}
