package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.dto.request.EditBannerRequest;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.util.Objects;
import lombok.Getter;

@Getter
public class EditBannerUseCase {

    private Long bannerId;
    private LanguageCode language;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;

    public EditBannerUseCase(Long bannerId, EditBannerRequest request, LanguageCode languageCode) {
        this.bannerId = bannerId;
        this.language = Objects.isNull(languageCode) ? LanguageCode.EN: languageCode;
        this.thumbnailImage = request.getThumbnailImage();
        this.mainTitle = request.getMainTitle();
        this.subTitle = request.getSubTitle();
    }
}
