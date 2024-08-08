package com.keypoint.keypointtravel.banner.dto.dto;

import com.keypoint.keypointtravel.global.enumType.banner.LargeCategory;
import com.keypoint.keypointtravel.global.enumType.banner.MiddleCategory;
import com.keypoint.keypointtravel.global.enumType.banner.SmallCategory;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonTourismDto {

    private Long id;
    private String mainTitle;
    private String subTitle;
    private String name;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int bannerLikesSize;
    private boolean isLiked;
    private String thumbnailImage;
    private LargeCategory cat1;
    private MiddleCategory cat2;
    private SmallCategory cat3;
}
