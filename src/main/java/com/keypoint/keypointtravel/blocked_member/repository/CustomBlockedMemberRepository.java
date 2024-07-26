package com.keypoint.keypointtravel.blocked_member.repository;

public interface CustomBlockedMemberRepository {

    boolean existsBlockedMember(Long blockedMemberId, Long memberId);

    long deleteByBlockedMemberIdAndMemberId(Long blockedMemberId, Long memberId);
}
