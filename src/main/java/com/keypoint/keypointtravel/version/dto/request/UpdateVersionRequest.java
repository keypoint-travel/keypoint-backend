package com.keypoint.keypointtravel.version.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateVersionRequest {

    @NotBlank
    public String version;
}
