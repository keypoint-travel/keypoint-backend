package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementContent {

    private String mainTitle;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;
}
