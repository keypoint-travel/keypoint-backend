package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BannerSummaryUseCase {

    private Long bannerId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private String thumbnailImage;
    private String title;
    private LocalDateTime modifyAt;

    static public BannerSummaryUseCase from(Banner banner) {
        return BannerSummaryUseCase.builder()
            .bannerId(banner.getId())
            .region(banner.getAreaCode().getDescription())
            .tourType(banner.getContentType().getDescription())
            .cat1(banner.getCat1().getDescription())
            .cat2(banner.getCat2().getDescription())
            .cat3(banner.getCat3().getDescription())
            .thumbnailImage(banner.getThumbnailImage())
            .title(banner.getThumbnailTitle())
            .modifyAt(banner.getModifyAt())
            .build();
    }
}
