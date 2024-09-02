package com.keypoint.keypointtravel.banner.dto.useCase.advertisement;

import com.keypoint.keypointtravel.banner.dto.request.EditBannerRequest;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.Getter;

@Getter
public class EditBannerUseCase {

    private Long bannerId;
    private LanguageCode language;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;

    public EditBannerUseCase(Long bannerId, EditBannerRequest request, String language) {
        this.bannerId = bannerId;
        this.language = findLanguageValue(language);
        this.thumbnailImage = request.getThumbnailImage();
        this.mainTitle = request.getMainTitle();
        this.subTitle = request.getSubTitle();
    }

    private LanguageCode findLanguageValue(String language) {
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
}
