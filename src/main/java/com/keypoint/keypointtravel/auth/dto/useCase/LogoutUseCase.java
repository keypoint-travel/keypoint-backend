package com.keypoint.keypointtravel.auth.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutUseCase {

    private String accessToken;

    public static LogoutUseCase from(String accessToken) {
        return new LogoutUseCase(accessToken);
    }
}