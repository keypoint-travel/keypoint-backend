package com.keypoint.keypointtravel.auth.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueUseCase {

    private String accessToken;
    private String refreshToken;

    public static ReissueUseCase of(String accessToken, String refreshToken) {
        return new ReissueUseCase(accessToken, refreshToken);
    }
}
