package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonBannerThumbnailUseCase {

    private String contentId;
    private String thumbnailImageUrl;
    private String mainTitle;
    private String subTitle;

    static public CommonBannerThumbnailUseCase from(Banner banner) {
        return CommonBannerThumbnailUseCase.builder()
            .contentId(String.valueOf(banner.getId()))
            .thumbnailImageUrl(banner.toString())
            .mainTitle(banner.toString())
            .subTitle(banner.toString())
            .build();
    }
}
