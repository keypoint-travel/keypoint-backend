package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.badge.dto.response.CommonBadgeResponse;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String email;
    private TokenInfoResponse token;
    private CommonBadgeResponse badge;

    public MemberResponse(
            Long id,
            String email,
            TokenInfoResponse token,
            String badgeUrl
    ) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.badge = new CommonBadgeResponse(
                MessageSourceUtils.getBadgeName(BadgeType.SIGN_UP),
                badgeUrl
        );
    }

    public static MemberResponse of(Member member, TokenInfoResponse token, String badgeUrl) {
        return new MemberResponse(member.getId(), member.getEmail(), token, badgeUrl);
    }
}
