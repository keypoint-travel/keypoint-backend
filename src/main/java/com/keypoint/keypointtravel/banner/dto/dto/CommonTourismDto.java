package com.keypoint.keypointtravel.banner.dto.dto;

import com.querydsl.core.annotations.QueryProjection;

public class CommonTourismDto {

    private Long bannerId;
    private String thumbNailTitle;
    private String title;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int totalLikes;
    private boolean myLike;

    @QueryProjection
    public CommonTourismDto(Long bannerId, String thumbNailTitle, String title, String address1, String address2, Double latitude, Double longitude, int totalLikes, boolean myLike) {
        this.bannerId = bannerId;
        this.thumbNailTitle = thumbNailTitle;
        this.title = title;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalLikes = totalLikes;
        this.myLike = myLike;
    }
}
