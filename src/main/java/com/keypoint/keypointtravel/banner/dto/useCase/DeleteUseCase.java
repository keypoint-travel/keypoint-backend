package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.Getter;

@Getter
public class DeleteUseCase {

    private Long bannerId;
    private LanguageCode languageCode;

    public DeleteUseCase(Long bannerId, String language) {
        this.bannerId = bannerId;
        this.languageCode = language == null ? null : findLanguageValue(language);
    }

    private static LanguageCode findLanguageValue(String language) {
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