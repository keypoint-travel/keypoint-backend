package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampaignErrorCode implements ErrorCode {

    NOT_EXISTED_CAMPAIGN("001_NOT_EXISTED_CAMPAIGN", "존재하지 않는 캠페인입니다."),
    NOT_CAMPAIGN_OWNER("002_NOT_CAMPAIGN_OWNER", "해당 캠페인 장이 아닙니다"),
    BLOCKED_MEMBER_IN_CAMPAIGN("003_BLOCKED_MEMBER_IN_CAMPAIGN", "해당 회원을 차단하는 회원이 캠페인 내에 있습니다"),
    DUPLICATED_MEMBER("004_DUPLICATED_MEMBER", "이미 가입된 캠페인 회원입니다");

    private final String code;
    private final String msg;
}
