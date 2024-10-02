package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonTourismDto {

    private Long id;
    private String contentId;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int bannerLikesSize;
    private boolean isLiked;
    private String thumbnailImage;
    private String cat1;
    private String cat2;
    private String cat3;
}
