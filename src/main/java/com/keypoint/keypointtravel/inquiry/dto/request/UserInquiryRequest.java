package com.keypoint.keypointtravel.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInquiryRequest {

    @NotEmpty(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
