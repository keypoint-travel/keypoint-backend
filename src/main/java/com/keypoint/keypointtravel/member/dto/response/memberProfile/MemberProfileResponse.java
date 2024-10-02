package com.keypoint.keypointtravel.member.dto.response.memberProfile;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileResponse {

    private Long memberId;

    private String name;

    private String email;

    private String profileImageUrl;

    private MemberAlarmResponse alarms;

    private LanguageCode language;

    private Long badgeCnt;

    private Long campaginCnt;

    private Boolean isPremiumMember;

    private String representativeBadgeUrl;

    private MemberThemeResponse themes;

    @QueryProjection
    public MemberProfileResponse(
        Long memberId,
        String name,
        String email,
        String profileImageUrl,
        LanguageCode language,
        MemberAlarmResponse alarms,
        Long badgeCnt,
        Long campaginCnt,
        String representativeBadgeUrl,
        Long premiumMemberCnt,
        MemberThemeResponse themes
    ) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.language = language;
        this.alarms = alarms;
        this.badgeCnt = badgeCnt;
        this.campaginCnt = campaginCnt;
        this.representativeBadgeUrl = representativeBadgeUrl;
        this.isPremiumMember = premiumMemberCnt > 0;
        this.themes = themes;
    }
}
