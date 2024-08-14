package com.keypoint.keypointtravel.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherMemberProfileResponse {

    private Long memberId;
    private String profileImage;
    private String memberName;
    private boolean isBlocked;
//    List<BadgeInfo> badges;    todo: 배지 기능 구현 후 이어서 구현 필요
}
