package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class AdvertisementUseCase {

    private MultipartFile thumbnailImage;
    private MultipartFile detailImage;
    private LanguageCode language;
    private String mainTitle;
    private String subTitle;
    private String content;
}
