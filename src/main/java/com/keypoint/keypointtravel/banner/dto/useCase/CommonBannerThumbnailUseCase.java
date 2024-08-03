package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonBannerThumbnailUseCase {

    private Long bannerId;
    private String thumbnailImageUrl;
    private String title;

    static public CommonBannerThumbnailUseCase from(Banner banner) {
        return CommonBannerThumbnailUseCase.builder()
            .bannerId(banner.getId())
            .thumbnailImageUrl(banner.getThumbnailImage())
            .title(banner.getThumbnailTitle())
            .build();
    }
}
