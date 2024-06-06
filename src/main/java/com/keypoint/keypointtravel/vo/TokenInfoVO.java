package com.keypoint.keypointtravel.vo;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TokenInfoVO {

    private String grandType;
    private String accessToken;
    private String refreshToken;

    public TokenInfoVO(
        String grandType,
        String accessToken,
        String refreshToken
    ) {
        this.grandType = grandType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenInfoVO toVO(
        String grandType,
        String accessToken,
        String refreshToken
    ) {
        return new TokenInfoVO(grandType, accessToken, refreshToken);
    }

}
