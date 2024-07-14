package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.BannerSummaryUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BannerListResponse {

    List<BannerSummaryUseCase> contents;

    public static BannerListResponse from(List<BannerSummaryUseCase> useCaseList) {
        return new BannerListResponse(useCaseList);
    }
}
