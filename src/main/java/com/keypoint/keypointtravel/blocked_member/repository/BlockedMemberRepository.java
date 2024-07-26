package com.keypoint.keypointtravel.blocked_member.repository;

import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberRepository extends JpaRepository<BlockedMember, Long>, CustomBlockedMemberRepository {

    boolean existsByBlockedMemberIdAndMemberId(Long blockedMemberId, Long memberId);
}
