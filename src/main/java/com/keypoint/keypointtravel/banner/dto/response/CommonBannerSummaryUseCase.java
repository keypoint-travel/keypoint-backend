package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.SummaryUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonBannerSummaryUseCase {

    private Long contentId;
    private List<SummaryUseCase> contents;

    public static CommonBannerSummaryUseCase from(Banner banner) {
        return new CommonBannerSummaryUseCase(
            banner.getId(), banner.getBannerContents().stream()
            .map(bannerContent -> SummaryUseCase.of(bannerContent, banner.getAreaCode())).toList());
    }
}
