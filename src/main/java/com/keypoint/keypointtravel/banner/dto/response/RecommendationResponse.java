package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.response.commonBanner.AroundTourism;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.banner.LargeCategory;
import com.keypoint.keypointtravel.global.enumType.banner.MiddleCategory;
import com.keypoint.keypointtravel.global.enumType.banner.SmallCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecommendationResponse {

    private String contentId;
    private String title;
    private String address1;
    private String address2;
    private String latitude;
    private String longitude;
    private String image;
    private String cat1;
    private String cat2;
    private String cat3;
    private List<AroundTourism> around;

    public static RecommendationResponse from(List<Item> items){
        return RecommendationResponse.builder()
            .contentId(items.get(0).getContentid())
            .title(items.get(0).getTitle())
            .address1(items.get(0).getAddr1())
            .address2(items.get(0).getAddr2())
            .latitude(items.get(0).getMapy())
            .longitude(items.get(0).getMapx())
            .image(items.get(0).getFirstimage())
            .cat1(BannerCode.getDescription(LargeCategory.class, items.get(0).getCat1()))
            .cat2(BannerCode.getDescription(MiddleCategory.class, items.get(0).getCat2()))
            .cat3(BannerCode.getDescription(SmallCategory.class, items.get(0).getCat3()))
            .around(items.stream()
                .skip(1)
                .map(AroundTourism::from).toList())
            .build();
    }
}
