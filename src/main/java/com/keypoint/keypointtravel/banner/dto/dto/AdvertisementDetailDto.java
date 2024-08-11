package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvertisementDetailDto {

    private Long bannerId;
    private String mainTitle;
    private String subTitle;
    private String content;
    private String detailImageUrl;
}
