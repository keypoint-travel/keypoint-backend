package com.keypoint.keypointtravel.banner.dto.request;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BannerRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "메인 제목의 최대길이를 초과하였습니다.")
    private String mainTitle;

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 40, message = "서브 제목의 최대길이를 초과하였습니다.")
    private String subTitle;

    @NotEmpty(message = "이미지 URL을 입력해주세요.")
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String thumbnailImage;

    @NotNull(message = "Language cannot be null.")
    private String placeName;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private String contentId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private LanguageCode languageCode;
}
