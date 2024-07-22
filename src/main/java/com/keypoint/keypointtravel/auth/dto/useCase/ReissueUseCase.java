package com.keypoint.keypointtravel.auth.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueUseCase {

    private String accessToken;

    public static ReissueUseCase from(String accessToken) {
        return new ReissueUseCase(accessToken);
    }
}
