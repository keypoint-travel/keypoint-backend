package com.keypoint.keypointtravel.oauth.dto.useCase.appleToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FailAppleTokenUseCase {

    @JsonProperty(value = "error")
    private String error;

    @JsonProperty(value = "error_description")
    private String errorDescription;

    @Override
    public String toString() {
        return String.format("%s %s", error, errorDescription);
    }
}
