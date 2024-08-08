package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvertisementThumbnailDto {

    private Long bannerId;
    private String thumbnailImageUrl;
    private String mainTitle;
    private String subTitle;
}
