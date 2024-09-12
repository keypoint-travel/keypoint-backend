package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.util.Objects;
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
    private String placeName;

    public static BannerDetails from(Item data, LanguageCode languageCode) {
        BannerDetails bannerDetails = BannerDetails.builder()
            .contentId(data.getContentid())
            .region(BannerCode.getDescription(AreaCode.class, data.getAreacode()))
            .address1(data.getAddr1())
            .address2(data.getAddr2())
            .latitude(data.getMapy())
            .longitude(data.getMapx())
            .thumbnailImage(data.getFirstimage())
            .placeName(data.getTitle())
            .build();
        bannerDetails.buildTypeByLanguage(data, languageCode);
        return bannerDetails;
    }

    private void buildTypeByLanguage(Item data, LanguageCode languageCode) {
        if (Objects.isNull(languageCode) || languageCode.equals(LanguageCode.EN)) {
            this.tourType = BannerCode.getDescription(ContentTypeByEng.class, data.getContenttypeid());
            this.cat1 = BannerCode.getDescription(LargeCategoryByEng.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategoryByEng.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategoryByEng.class, data.getCat3());
            return;
        }
        if (languageCode.equals(LanguageCode.KO)) {
            this.tourType = BannerCode.getDescription(ContentType.class, data.getContenttypeid());
            this.cat1 = BannerCode.getDescription(LargeCategory.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategory.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategory.class, data.getCat3());
            return;
        }
        if (languageCode.equals(LanguageCode.JA)) {
            this.tourType = BannerCode.getDescription(ContentTypeByJap.class, data.getContenttypeid());
            this.cat1 = BannerCode.getDescription(LargeCategoryByJap.class, data.getCat1());
            this.cat2 = BannerCode.getDescription(MiddleCategoryByJap.class, data.getCat2());
            this.cat3 = BannerCode.getDescription(SmallCategoryByJap.class, data.getCat3());
        }
    }
}