package com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Body {
    private Items items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
