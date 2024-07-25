package com.keypoint.keypointtravel.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {
    @NotBlank
    private String name;
}
