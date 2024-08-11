package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonBannerThumbnailDto {

    private Long contentId;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
}
