package com.keypoint.keypointtravel.member.dto.response.otherMemberProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EarnedBadgeResponse {

    private Long badgeId;
    private String badgeName;
    private int order;
    private String badgeImage;
}
