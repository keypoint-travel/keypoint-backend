package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdvertisementBannerUseCase {

    private Long bannerId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String detailUrl;
    private LocalDateTime createAt;
    private String writerName;
    private LocalDateTime modifyAt;
    private String modifierName;

    public static AdvertisementBannerUseCase from(AdvertisementBannerDto dto) {
        return new AdvertisementBannerUseCase(
            dto.getBannerId(),
            dto.getTitle(),
            dto.getContent(),
            dto.getThumbnailUrl(),
            dto.getDetailUrl(),
            dto.getCreateAt(),
            dto.getWriterName(),
            dto.getModifyAt(),
            dto.getModifierName()
        );
    }
}
