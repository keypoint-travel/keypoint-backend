package com.keypoint.keypointtravel.banner.dto.dto;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManageCommonTourismDto {

    private Long id;
    private String contentId;
    private LanguageCode languageCode;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int bannerLikesSize;
    private String thumbnailImage;
    private String cat1;
    private String cat2;
    private String cat3;
}
