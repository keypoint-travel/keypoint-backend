package com.keypoint.keypointtravel.banner.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdvertisementRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목의 최대길이를 초과하였습니다.")
    private String mainTitle;

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 40, message = "제목의 최대길이를 초과하였습니다.")
    private String subTitle;

    @NotEmpty(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;

    public AdvertisementRequest(String mainTitle, String subTitle, String content,
        LanguageCode languageCode) {
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.content = content;
        this.languageCode = languageCode != null ?languageCode : LanguageCode.EN;
    }
}
