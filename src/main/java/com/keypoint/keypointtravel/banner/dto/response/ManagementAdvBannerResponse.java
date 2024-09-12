package com.keypoint.keypointtravel.banner.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManagementAdvBannerResponse {

    private Long contentId;
    private String detailImage;
    private List<AdvInfo> contents;

    public ManagementAdvBannerResponse(Long contentId, String detailImage) {
        this.contentId = contentId;
        this.detailImage = detailImage;
        this.contents = new ArrayList<>();
    }
}
