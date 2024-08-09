package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.useCase.AdvertisementContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AdvertisementBannerUseCase {

    private String contentId;
    private String thumbnailImage;
    private String detailImage;
    private List<AdvertisementContent> contents;
}
