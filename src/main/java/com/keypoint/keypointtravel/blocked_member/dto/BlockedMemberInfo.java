package com.keypoint.keypointtravel.blocked_member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedMemberInfo {

    private Long memberId;
    private String memberName;

    public static BlockedMemberInfo from(BlockedMemberDto dto){
        return new BlockedMemberInfo(dto.getMemberId(), dto.getMemberName());
    }
}
