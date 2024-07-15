package com.keypoint.keypointtravel.banner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BannerRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목의 최대길이를 초과하였습니다.")
    private String thumbnailTitle;

    @NotEmpty(message = "이미지 URL을 입력해주세요.")
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String thumbnailImage;

    private String title;
    private Double latitude;
    private Double longitude;
    private Long contentId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;

}
