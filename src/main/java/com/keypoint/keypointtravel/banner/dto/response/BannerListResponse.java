package com.keypoint.keypointtravel.banner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BannerListResponse {

    private List<CommonBannerSummaryUseCase> banners;
}
