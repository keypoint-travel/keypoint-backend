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
        // 로그인 하지 않은 경우 myId는 -1로 설정(myId가 -1일 경우 조회하려는 회원의 차단 여부를 확인하지 않는다.)
        this.myId = userDetails == null ? null : userDetails.getId();
        this.otherMemberId = otherMemberId;
    }
}