package com.keypoint.keypointtravel.place.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRecentPlaceSearchRequest {

    @NotNull
    private Long placeId;
}
