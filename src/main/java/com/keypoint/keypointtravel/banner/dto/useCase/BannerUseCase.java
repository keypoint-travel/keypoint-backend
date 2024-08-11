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

    public BannerUseCase(LanguageCode languageCode, Long bannerId, CustomUserDetails userDetails) {
        this.languageCode = languageCode;
        this.bannerId = bannerId;
        this.memberId = userDetails.getId();
    }
}
