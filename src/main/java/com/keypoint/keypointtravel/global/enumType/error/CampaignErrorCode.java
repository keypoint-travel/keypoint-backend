package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampaignErrorCode implements ErrorCode {

    NOT_EXISTED_CAMPAIGN("001_NOT_EXISTED_CAMPAIGN", "존재하지 않는 캠페인입니다."),
    NOT_CAMPAIGN_OWNER("002_NOT_CAMPAIGN_OWNER", "해당 캠페인 장이 아닙니다"),
    BLOCKED_MEMBER_IN_CAMPAIGN("003_BLOCKED_MEMBER_IN_CAMPAIGN", "해당 회원을 차단하는 회원이 캠페인 내에 있습니다"),
    DUPLICATED_MEMBER("004_DUPLICATED_MEMBER", "이미 가입된 캠페인 회원입니다"),
    EXPIRED_INVITE_EMAIL("005_EXPIRED_INVITE_EMAIL", "초대된 이력이 없거나, 유효기간이 만료되었습니다"),
    NOT_EXISTED_CURRENCY("006_NOT_EXISTED_CURRENCY", "존재하지 않는 화폐입니다"),
    ALREADY_IN_WAIT_LIST("007_ALREADY_IN_WAIT_LIST", "이미 대기명단에 있습니다"),
    NOT_EXISTED_CAMPAIGN_WAIT_MEMBER("008_NOT_EXISTED_CAMPAIGN_WAIT_MEMBER", "회원이 대기명단에 존재하지 않습니다"),
    MULTIPLE_CAMPAIGN_NON_PREMIUM("009_MANY_CAMPAIGN_NOT_PREMIUM_MEMBER", "프리미엄 회원이 아닌 회원은 하나의 캠페인 참여만 가능합니다."),
    CANNOT_INVITE_SELF("010_CANNOT_INVITE_SELF", "자기 자신을 초대할 수 없습니다."),
    ALREADY_INVITE_EMAIL("011_ALREADY_INVITE_EMAIL", "이미 초대된 이메일입니다."),
    PROHIBIT_INVITE_EMAIL("012_PROHIBIT_INVITE_EMAIL", "3회 이상 재전송하였습니다. 하루동안 이메일로 초대할 수 없습니다."),
    NOT_MATCH_BUDGET("013_NOT_MATCH_BUDGET", "총 예산이 일치하지 않습니다."),
    NOT_EXISTED_BUDGET("014_NOT_EXISTED_BUDGET", "예산을 하나 이상 입력해주세요."),
    MEMBER_CAMPAIGN_PERIOD_OVERLAP("015_MEMBER_CAMPAIGN_PERIOD_OVERLAP", "기간이 겹치는 다른 캠페인을 진행중인 회원입니다."),
    FRIEND_CAMPAIGN_PERIOD_OVERLAP("016_FRIEND_CAMPAIGN_PERIOD_OVERLAP", "다른 캠페인과 기간이 겹치는 회원이 있습니다.");

    private final String code;
    private final String msg;
}
