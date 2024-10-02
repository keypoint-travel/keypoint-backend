package com.keypoint.keypointtravel.banner.dto.useCase.advertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementContent {

    private String language;
    private String mainTitle;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;
}
