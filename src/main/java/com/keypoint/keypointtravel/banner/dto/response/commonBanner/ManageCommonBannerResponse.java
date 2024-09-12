package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ManageCommonBannerResponse {

    private Long contentId;
    private Double latitude;
    private Double longitude;
    private List<ManageCommonInfo> contents;

    public ManageCommonBannerResponse(Long contentId, Double latitude, Double longitude) {
        this.contentId = contentId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contents = new ArrayList<>();
    }
}
