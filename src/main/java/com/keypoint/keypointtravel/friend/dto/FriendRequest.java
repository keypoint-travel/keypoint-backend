package com.keypoint.keypointtravel.friend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @NotEmpty(message = "값을 입력해주세요.")
    @NotBlank(message = "값을 입력해주세요.")
    private String invitationValue;
}
