package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementBannerUseCase {

    private Long contentId;
    private String mainTitle;
    private String subTitle;
    private String content;
    private String thumbnailImage;
    private String detailImage;
    private LocalDateTime createdAt;
    private String writerName;
    private LocalDateTime updatedAt;
    private String updaterName;

    public static AdvertisementBannerUseCase from(AdvertisementBannerDto dto) {
        return new AdvertisementBannerUseCase(
            dto.getBannerId(),
            dto.getMainTitle(),
            dto.getSubTitle(),
            dto.getContent(),
            dto.getThumbnailUrl(),
            dto.getDetailUrl(),
            dto.getCreatedAt(),
            dto.getWriterName(),
            dto.getUpdatedAt(),
            dto.getUpdaterName()
        );
    }
}
