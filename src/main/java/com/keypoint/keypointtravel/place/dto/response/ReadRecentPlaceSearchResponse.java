package com.keypoint.keypointtravel.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadRecentPlaceSearchResponse {

    private String placeSearchHistoryId;
    private String searchWord;

    public static ReadRecentPlaceSearchResponse of(
        String placeSearchHistoryId, String searchWord
    ) {
        return new ReadRecentPlaceSearchResponse(placeSearchHistoryId, searchWord);
    }
}
