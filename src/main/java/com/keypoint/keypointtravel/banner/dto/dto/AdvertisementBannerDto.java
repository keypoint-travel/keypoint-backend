package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementBannerDto {

    private Long bannerId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String detailUrl;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;
}
