package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import com.keypoint.keypointtravel.global.constants.TourismApiConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AroundTourism {

    private String contentId;
    private String image;
    private String placeName;

    public static AroundTourism from(Item data) {
        String imagePath = data.getFirstimage();
        if(imagePath == null || imagePath.isEmpty() || imagePath.isBlank()) {
            imagePath = TourismApiConstants.DEFAULT_IMAGE;
        }
        return new AroundTourism(data.getContentid(), imagePath, data.getTitle());
    }
}
