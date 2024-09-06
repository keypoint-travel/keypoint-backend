package com.keypoint.keypointtravel.visitedCountry.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchVisitedCountryUseCase {
    private Long memberId;
    private String keyword;
}
