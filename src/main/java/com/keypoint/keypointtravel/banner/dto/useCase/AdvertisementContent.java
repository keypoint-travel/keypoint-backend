package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
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

    public static AdvertisementContent from(AdvertisementBannerDto dto) {
        return new AdvertisementContent(
            dto.getLanguageCode().getDescription(),
            dto.getMainTitle(),
            dto.getSubTitle(),
            dto.getContent(),
            dto.getCreatedAt(),
            dto.getWriterName(),
            dto.getUpdatedAt(),
            dto.getUpdaterName());
    }
}
