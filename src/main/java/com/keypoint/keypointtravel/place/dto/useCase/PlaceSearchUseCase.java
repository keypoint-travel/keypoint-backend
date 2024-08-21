package com.keypoint.keypointtravel.place.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceSearchUseCase {

    private Long memberId;
    private String searchWord;

    public static PlaceSearchUseCase of(Long id, String searchWord) {
        return new PlaceSearchUseCase(id, searchWord);
    }
}
