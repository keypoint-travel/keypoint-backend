package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.response.commonBanner.AroundTourism;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecommendationResponse {

    private String contentId;
    private String memberName;
    private String placeName;
    private String address1;
    private String address2;
    private String latitude;
    private String longitude;
    private String image;
    private String cat1;
    private String cat2;
    private String cat3;
    private List<AroundTourism> around;

    public static RecommendationResponse of(List<Item> items, String memberName, String language){
        RecommendationResponse response = RecommendationResponse.builder()
            .contentId(items.get(0).getContentid())
            .memberName(memberName)
            .placeName(items.get(0).getTitle())
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
        response.buildTypeByLanguage(items.get(0), language);
        return  response;
    }

    private void buildTypeByLanguage(Item data, String language) {
        if (language.equals("kor")) {
            this.cat1 = BannerCode.getDescription(LargeCategory.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategory.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategory.class, data.getCat3());
        }
        if (language.equals("eng")) {
            this.cat1 = BannerCode.getDescription(LargeCategoryByEng.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategoryByEng.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategoryByEng.class, data.getCat3());
        }
        if (language.equals("jap")) {
            this.cat1 = BannerCode.getDescription(LargeCategoryByJap.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategoryByJap.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategoryByJap.class, data.getCat3());
        }
    }
}
