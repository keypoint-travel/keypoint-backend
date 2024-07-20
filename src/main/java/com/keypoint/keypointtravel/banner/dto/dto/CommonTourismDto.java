package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonTourismDto {

    private Long id;
    private String thumbnailTitle;
    private String title;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int bannerLikesSize;
    private boolean isLiked;
}
