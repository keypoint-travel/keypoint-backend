package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementBannerDto {

    private Long contentId;
    private String thumbnailImage;
    private String detailImage;
    private String mainTitle;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;
}
