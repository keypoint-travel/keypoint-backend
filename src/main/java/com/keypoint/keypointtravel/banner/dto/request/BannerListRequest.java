package com.keypoint.keypointtravel.banner.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BannerListRequest {

    private String language;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private int page;
}
