package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.AdvertisementContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdvertisementBannerUseCase {

    private String contentId;
    private String thumbnailImage;
    private String detailImage;
    private List<AdvertisementContent> contents;

    public void sortContents() {
        contents.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
    }
}
