package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ThumbnailUseCase {

    private Long bannerId;
    private String thumbnailImage;
    private String title;

    static public ThumbnailUseCase from(Banner banner) {
        return ThumbnailUseCase.builder()
            .bannerId(banner.getId())
            .thumbnailImage(banner.getThumbnailImage())
            .title(banner.getThumbnailTitle())
            .build();
    }
}
