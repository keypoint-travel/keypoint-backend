package com.keypoint.keypointtravel.dto.auth.response;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TokenInfoDTO {

    private String grandType;
    private String accessToken;
    private String refreshToken;

    public TokenInfoDTO(
        String grandType,
        String accessToken,
        String refreshToken
    ) {
        this.grandType = grandType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenInfoDTO toDTO(
        String grandType,
        String accessToken,
        String refreshToken
    ) {
        return new TokenInfoDTO(grandType, accessToken, refreshToken);
    }

}
