package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.Getter;

@Getter
public class BannerUseCase {

    private Long bannerId;
    private Long memberId;

    public BannerUseCase(Long bannerId, CustomUserDetails userDetails) {
        this.bannerId = bannerId;
        this.memberId = userDetails == null ? null : userDetails.getId();
    }
}
