package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.Getter;

@Getter
public class BannerUseCase {

    private LanguageCode languageCode;
    private Long bannerId;
    private Long memberId;

    public BannerUseCase(String languageCode, Long bannerId, CustomUserDetails userDetails) {
        this.languageCode = findLanguageValue(languageCode);
        this.bannerId = bannerId;
        this.memberId = userDetails.getId();
    }

    private LanguageCode findLanguageValue(String language) {
        if (language.equals("kor")) {
            return LanguageCode.KO;
        }
        if (language.equals("eng")) {
            return LanguageCode.EN;
        }
        if (language.equals("jap")) {
            return LanguageCode.JA;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }
}
