package com.keypoint.keypointtravel.blocked_member.repository;


import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberDto;

import java.util.List;

public interface CustomBlockedMemberRepository {

    boolean existsBlockedMember(Long blockedMemberId, Long memberId);

    long deleteByBlockedMemberIdAndMemberId(Long blockedMemberId, Long memberId);

    List<BlockedMemberDto> findBlockedMembers(Long memberId);

    boolean existsBlockedMembers(List<Long> memberIds);

    boolean existsBlockedMembers(List<Long> memberIds, List<Long> targetIds);

    boolean existsBlockedMembers(List<Long> memberIds, Long targetId);
}
