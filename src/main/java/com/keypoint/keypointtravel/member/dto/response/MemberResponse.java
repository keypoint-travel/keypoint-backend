package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String email;
    private TokenInfoResponse token;
    private String name;
    private String badgeUrl;

    public MemberResponse(
            Long id,
            String email,
            TokenInfoResponse token,
            String badgeUrl
    ) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.badgeUrl = badgeUrl;
        this.name = MessageSourceUtils.getBadgeName(BadgeType.SIGN_UP);
    }

    public static MemberResponse of(Member member, TokenInfoResponse token, String badgeUrl) {
        return new MemberResponse(member.getId(), member.getEmail(), token, badgeUrl);
    }
}
