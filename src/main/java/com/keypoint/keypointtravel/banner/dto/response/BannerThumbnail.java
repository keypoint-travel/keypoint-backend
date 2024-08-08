package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.global.enumType.banner.BannerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BannerThumbnail {

    private String contentId;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
    private BannerType bannerType;
}
