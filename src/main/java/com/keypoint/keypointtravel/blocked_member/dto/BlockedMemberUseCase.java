package com.keypoint.keypointtravel.blocked_member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedMemberUseCase {

    private Long blockedMemberId;
    private Long myId;
}
