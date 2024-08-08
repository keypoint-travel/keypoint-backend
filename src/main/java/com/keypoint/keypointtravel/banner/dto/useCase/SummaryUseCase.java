package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SummaryUseCase {

    private String contentId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
    private String name;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    static public SummaryUseCase from(Banner banner) {
        return SummaryUseCase.builder()
            .contentId(String.valueOf(banner.getId()))
            .region(banner.getAreaCode().getDescription())
            .tourType(banner.getContentType().getDescription())
            .cat1(banner.getCat1().getDescription())
            .cat2(banner.getCat2().getDescription())
            .cat3(banner.getCat3().getDescription())
            .thumbnailImage(banner.getThumbnailImage())
            .mainTitle(banner.getMainTitle())
            .subTitle(banner.getSubTitle())
            .name(banner.getName())
            .updatedAt(banner.getModifyAt())
            .createdAt(banner.getCreateAt())
            .build();
    }
}
