package com.keypoint.keypointtravel.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateRecentPlaceSearchRequest {

    @NotBlank
    private String searchWord;
}
