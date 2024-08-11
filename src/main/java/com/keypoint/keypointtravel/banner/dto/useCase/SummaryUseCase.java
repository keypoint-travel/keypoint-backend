package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SummaryUseCase {

    private String language;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    static public SummaryUseCase of(BannerContent bannerContent, AreaCode areaCode) {
        return SummaryUseCase.builder()
            .language(bannerContent.getLanguageCode().getDescription())
            .region(getRegion(areaCode, bannerContent.getLanguageCode()))
            .tourType(bannerContent.getContentType())
            .cat1(bannerContent.getCat1())
            .cat2(bannerContent.getCat2())
            .cat3(bannerContent.getCat3())
            .thumbnailImage(bannerContent.getThumbnailImage())
            .mainTitle(bannerContent.getMainTitle())
            .subTitle(bannerContent.getSubTitle())
            .placeName(bannerContent.getPlaceName())
            .updatedAt(bannerContent.getModifyAt())
            .createdAt(bannerContent.getCreateAt())
            .build();
    }

    static private String getRegion(AreaCode areaCode, LanguageCode language) {
        if (language.equals(LanguageCode.KO)) {
            return areaCode.getDescription();
        }
        if (language.equals(LanguageCode.EN)) {
            return areaCode.getEngDescription();
        }
        if (language.equals(LanguageCode.JA)) {
            return areaCode.getJapDescription();
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }
}
