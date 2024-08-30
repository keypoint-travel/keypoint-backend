package com.keypoint.keypointtravel.badge.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateBadgeRequest {
    @Min(0)
    private int order;
}
