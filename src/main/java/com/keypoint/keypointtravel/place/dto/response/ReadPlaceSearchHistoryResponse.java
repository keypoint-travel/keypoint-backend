package com.keypoint.keypointtravel.place.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadPlaceSearchHistoryResponse {

    private Long placeSearchHistoryId;
    private String searchWord;
}
