package com.keypoint.keypointtravel.oauth.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueRefreshTokenUseCase {

    private boolean isNeedToReissue;
    private String refreshToken;

    public static ReissueRefreshTokenUseCase of(boolean isNeedToReissue, String refreshToken) {
        return new ReissueRefreshTokenUseCase(isNeedToReissue, refreshToken);
    }

}
