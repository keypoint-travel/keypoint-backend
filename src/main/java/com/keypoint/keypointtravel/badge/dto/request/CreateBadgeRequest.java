package com.keypoint.keypointtravel.badge.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateBadgeRequest {

    @NotBlank
    private String name;
    @Min(0)
    private int order;
}
