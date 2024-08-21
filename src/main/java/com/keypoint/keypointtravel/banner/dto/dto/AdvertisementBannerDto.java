package com.keypoint.keypointtravel.banner.dto.dto;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementBannerDto {

    private Long bannerId;
    private String thumbnailUrl;
    private String detailUrl;
    private String mainTitle;
    private String subTitle;
    private String content;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;
    private LanguageCode languageCode;
}
