package com.keypoint.keypointtravel.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsExistedResponse {

    private Boolean isExisted;

    public static IsExistedResponse from(boolean isExisted) {
        return new IsExistedResponse(isExisted);
    }
}
