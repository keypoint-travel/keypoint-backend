package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import com.keypoint.keypointtravel.banner.entity.AreaCode;
import com.keypoint.keypointtravel.banner.entity.BannerCode;
import com.keypoint.keypointtravel.banner.entity.ContentType;
import com.keypoint.keypointtravel.banner.entity.LargeCategory;
import com.keypoint.keypointtravel.banner.entity.MiddleCategory;
import com.keypoint.keypointtravel.banner.entity.SmallCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BannerDetails {

    private String contentId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private String address1;
    private String address2;
    private String latitude;
    private String longitude;
    private String thumbnailImage;
    private String title;

    public static BannerDetails from(Item data) {
        return BannerDetails.builder()
            .contentId(data.getContentid())
            .region(BannerCode.getDescription(AreaCode.class, data.getAreacode()))
            .tourType(BannerCode.getDescription(ContentType.class, data.getContenttypeid()))
            .cat1(BannerCode.getDescription(LargeCategory.class, data.getCat1()))
            .cat2(BannerCode.getDescription(MiddleCategory.class, data.getCat2()))
            .cat3(BannerCode.getDescription(SmallCategory.class, data.getCat3()))
            .address1(data.getAddr1())
            .address2(data.getAddr2())
            .latitude(data.getMapx())
            .longitude(data.getMapy())
            .thumbnailImage(data.getFirstimage())
            .title(data.getTitle())
            .build();
    }
}