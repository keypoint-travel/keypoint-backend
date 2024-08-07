package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.global.enumType.banner.BannerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BannerThumbnail {

    private String contentId;
    private String thumbnailImageUrl;
    private String title;
    private BannerType bannerType;
}
