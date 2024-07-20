package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;

import java.util.Objects;

public record DeleteUseCase(Long bannerId) {

    public DeleteUseCase {
        if (Objects.isNull(bannerId)) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }
}