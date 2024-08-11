package com.keypoint.keypointtravel.oauth.dto.useCase.googleUserInfoUseCase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfoUseCase {

    private String id;

    private String email;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    private String name;

    @JsonProperty("given_name")
    private String given_name;

    @JsonProperty("family_name")
    private String family_name;

    private String picture;

}
