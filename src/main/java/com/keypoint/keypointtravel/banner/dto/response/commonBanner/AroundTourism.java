package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AroundTourism {

    private String contentId;
    private String image;
    private String name;

    public static AroundTourism from(Item data) {
        return new AroundTourism(data.getContentid(), data.getFirstimage(), data.getTitle());
    }
}
