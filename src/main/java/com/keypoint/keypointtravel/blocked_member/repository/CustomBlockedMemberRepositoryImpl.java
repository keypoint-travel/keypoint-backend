package com.keypoint.keypointtravel.blocked_member.repository;

import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBlockedMemberRepositoryImpl implements CustomBlockedMemberRepository {

    private final JPAQueryFactory queryFactory;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    @Override
    public boolean existsBlockedMember(Long blockedMemberId, Long memberId) {
        // 상대가 나를, 내가 상대를 차단한 경우가 있는지 확인
        BlockedMember member = queryFactory.selectFrom(blockedMember)
            .where((blockedMember.blockedMemberId.eq(blockedMemberId).and(blockedMember.member.id.eq(memberId)))
                .or(blockedMember.blockedMemberId.eq(memberId).and(blockedMember.member.id.eq(blockedMemberId))))
            .fetchFirst();
        return member != null;
    }
}
