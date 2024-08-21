package com.keypoint.keypointtravel.campaign.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteByEmailRequest {

    @NotEmpty(message = "이메일은 공백이 될 수 없습니다.")
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;

    private Long campaignId;
}
