package com.keypoint.keypointtravel.place.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceIdUseCase {

    private Long memberId;
    private Long placeId;

    public static PlaceIdUseCase of(Long id, Long placeId) {
        return new PlaceIdUseCase(id, placeId);
    }
}
