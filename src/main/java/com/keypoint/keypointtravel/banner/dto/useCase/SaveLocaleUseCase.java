package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.dto.request.BannerLocaleRequest;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.banner.ContentTypeByEng;
import com.keypoint.keypointtravel.global.enumType.banner.LargeCategoryByEng;
import com.keypoint.keypointtravel.global.enumType.banner.MiddleCategoryByEng;
import com.keypoint.keypointtravel.global.enumType.banner.SmallCategoryByEng;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SaveLocaleUseCase {

    private Long bannerId;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private String address1;
    private String address2;
    private LanguageCode languageCode;

    public static SaveLocaleUseCase of(BannerLocaleRequest request, Long bannerId) {
        return new SaveLocaleUseCase(
            bannerId,
            request.getMainTitle(),
            request.getSubTitle(),
            request.getPlaceName(),
            request.getAddress1(),
            request.getAddress2(),
            request.getLanguageCode()
        );
    }

    public BannerContent toEntity(BannerContent bannerContent) {
        String tourType = "tourism." + BannerCode.getConstant(ContentTypeByEng.class, bannerContent.getContentType()).getCode();
        String cat1 = "tourism." + BannerCode.getConstant(LargeCategoryByEng.class, bannerContent.getCat1()).getCode();
        String cat2 = "tourism." + BannerCode.getConstant(MiddleCategoryByEng.class, bannerContent.getCat2()).getCode();
        String cat3 = "tourism." + BannerCode.getConstant(SmallCategoryByEng.class, bannerContent.getCat3()).getCode();
        return BannerContent.builder()
            .banner(bannerContent.getBanner())
            .contentId(bannerContent.getContentId())
            .mainTitle(mainTitle)
            .subTitle(subTitle)
            .placeName(placeName)
            .thumbnailImage(bannerContent.getThumbnailImage())
            .address1(address1)
            .address2(address2)
            .languageCode(languageCode)
            .contentType(MessageSourceUtils.getLocalizedLanguage(tourType, languageCode.getLocale()))
            .cat1(MessageSourceUtils.getLocalizedLanguage(cat1, languageCode.getLocale()))
            .cat2(MessageSourceUtils.getLocalizedLanguage(cat2, languageCode.getLocale()))
            .cat3(MessageSourceUtils.getLocalizedLanguage(cat3, languageCode.getLocale()))
            .isDeleted(false)
            .build();
    }
}
