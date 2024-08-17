package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampaignErrorCode implements ErrorCode{

    NOT_EXISTED_CAMPAIGN("001_NOT_EXISTED_CAMPAIGN", "존재하지 않는 캠페인입니다.");

    private final String code;
    private final String msg;
}
