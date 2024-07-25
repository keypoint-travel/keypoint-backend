package com.keypoint.keypointtravel.friend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @NotEmpty(message = "값을 입력해주세요.")
    @NotBlank(message = "값을 입력해주세요.")
    @Size(max = 30, message = "최대길이를 초과하였습니다.")
    private String invitationValue;
}
