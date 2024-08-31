package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.dto.request.BannerRequest;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.global.enumType.banner.AreaCode;
import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveUseCase {

    private String contentId;
    private LanguageCode languageCode;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private AreaCode areaCode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentType;
    private String thumbnailImage;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;

    public static SaveUseCase of(BannerRequest request, String language) {
        return SaveUseCase.builder()
            .contentId(request.getContentId())
            .languageCode(findLanguageValue(language))
            .mainTitle(request.getMainTitle())
            .subTitle(request.getSubTitle())
            .placeName(request.getPlaceName())
            .areaCode(BannerCode.getConstant(AreaCode.class, request.getRegion()))
            .cat1(request.getCat1())
            .cat2(request.getCat2())
            .cat3(request.getCat3())
            .contentType(request.getTourType())
            .thumbnailImage(request.getThumbnailImage())
            .address1(request.getAddress1())
            .address2(request.getAddress2())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .build();
    }

    private static LanguageCode findLanguageValue(String language) {
        if (language.equals("ko")) {
            return LanguageCode.KO;
        }
        if (language.equals("en")) {
            return LanguageCode.EN;
        }
        if (language.equals("ja")) {
            return LanguageCode.JA;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }

    public Banner toEntity(boolean isDeleted) {
        return Banner.builder()
            .areaCode(this.areaCode)
            .latitude(this.latitude)
            .longitude(this.longitude)
            .isDeleted(isDeleted)
            .build();
    }

    public BannerContent toEntity(Banner banner, boolean isDeleted) {
        return BannerContent.builder()
            .languageCode(this.languageCode)
            .banner(banner)
            .contentId(this.contentId)
            .mainTitle(this.mainTitle)
            .subTitle(this.subTitle)
            .placeName(this.placeName)
            .thumbnailImage(this.thumbnailImage)
            .cat1(this.cat1)
            .cat2(this.cat2)
            .cat3(this.cat3)
            .contentType(this.contentType)
            .address1(this.address1)
            .address2(this.address2)
            .isDeleted(isDeleted)
            .build();
    }
}