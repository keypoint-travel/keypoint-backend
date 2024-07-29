package com.keypoint.keypointtravel.banner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvertisementRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목의 최대길이를 초과하였습니다.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, message = "내용의 최대길이를 초과하였습니다.")
    private String content;
}
