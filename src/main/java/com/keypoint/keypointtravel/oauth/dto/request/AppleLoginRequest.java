package com.keypoint.keypointtravel.oauth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AppleLoginRequest {

    @NotNull
    private String oauthCode;

    private String name;
}
