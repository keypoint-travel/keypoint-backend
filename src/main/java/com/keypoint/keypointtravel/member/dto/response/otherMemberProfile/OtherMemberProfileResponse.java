package com.keypoint.keypointtravel.member.dto.response.otherMemberProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OtherMemberProfileResponse {

    private Long memberId;
    private String profileImage;
    private String memberName;
    private boolean isBlocked;
    List<EarnedBadgeResponse> badges;

    public OtherMemberProfileResponse(Long memberId, String profileImage, String memberName, boolean isBlocked) {
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.memberName = memberName;
        this.isBlocked = isBlocked;
    }

    public void addBadges(List<EarnedBadgeResponse> badges){
        this.badges = badges;
    }
}
