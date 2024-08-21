package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherMemberUseCase {

    public Long myId;
    public Long otherMemberId;

    public OtherMemberUseCase (Long otherMemberId, CustomUserDetails userDetails) {
        this.myId = userDetails.getId();
        this.otherMemberId = otherMemberId;
    }
}