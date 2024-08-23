package com.keypoint.keypointtravel.member.dto.response.memberProfile;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;

@Getter
public class MemberProfileResponse {

    private String name;

    private String email;

    private String profileImageUrl;

    private MemberAlarmResponse alarms;

    private LanguageCode language;

    private Long badgeCnt; // TODO 추후 이어서 구현 필요

    private Long campaginCnt; // TODO 추후 이어서 구현 필요

    private Boolean isPremiumMember; // TODO 추후 이어서 구현 필요

    private String representativeBadgeUrl; // TODO 추후 이어서 구현 필요

    public MemberProfileResponse() {
        this.badgeCnt = 0L;
        this.campaginCnt = 0L;
        this.isPremiumMember = false;
    }
}
