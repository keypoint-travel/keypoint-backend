package com.keypoint.keypointtravel.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsExistedEmailResponse {

    private Boolean isExisted;

    public static IsExistedEmailResponse from(boolean isExisted) {
        return new IsExistedEmailResponse(isExisted);
    }
}
