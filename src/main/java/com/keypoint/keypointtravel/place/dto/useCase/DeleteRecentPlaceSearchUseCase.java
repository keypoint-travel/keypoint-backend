package com.keypoint.keypointtravel.place.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteRecentPlaceSearchUseCase {

    private Long memberId;
    private Long placeSearchHistoryId;

    public static DeleteRecentPlaceSearchUseCase of(Long id, Long placeSearchHistoryId) {
        return new DeleteRecentPlaceSearchUseCase(id, placeSearchHistoryId);
    }
}
