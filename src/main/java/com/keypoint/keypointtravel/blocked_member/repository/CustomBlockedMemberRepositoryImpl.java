package com.keypoint.keypointtravel.blocked_member.repository;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberDto;
import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBlockedMemberRepositoryImpl implements CustomBlockedMemberRepository {

    private final JPAQueryFactory queryFactory;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    @Override
    public boolean existsBlockedMember(Long blockedMemberId, Long memberId) {
        // 상대가 나를, 내가 상대를 차단한 경우가 있는지 확인
        BlockedMember member = queryFactory.selectFrom(blockedMember)
            .where((blockedMember.blockedMemberId.eq(blockedMemberId)
                .and(blockedMember.member.id.eq(memberId)))
                .or(blockedMember.blockedMemberId.eq(memberId)
                    .and(blockedMember.member.id.eq(blockedMemberId))))
            .fetchFirst();
        return member != null;
    }

    @Override
    public long deleteByBlockedMemberIdAndMemberId(Long blockedMemberId, Long memberId) {
        return queryFactory.delete(blockedMember)
            .where(blockedMember.blockedMemberId.eq(blockedMemberId),
                blockedMember.member.id.eq(memberId))
            .execute();
    }

    @Override
    public List<BlockedMemberDto> findBlockedMembers(Long memberId) {
        QMemberDetail memberDetail = QMemberDetail.memberDetail;
        // 내가 차단한 회원 목록 조회
        return queryFactory.select(Projections.constructor(BlockedMemberDto.class,
                blockedMember.blockedMemberId,
                memberDetail.member.name))
            .from(blockedMember)
            .innerJoin(memberDetail).on(blockedMember.blockedMemberId.eq(memberDetail.member.id))
            .where(blockedMember.member.id.eq(memberId))
            .orderBy(blockedMember.createAt.desc())
            .fetch();
    }

    @Override
    public boolean existsBlockedMembers(List<Long> memberIds) {
        // 회원들 중 서로가 차단한 경우가 있는지 확인
        BlockedMember member = queryFactory.selectFrom(blockedMember)
            .where(blockedMember.blockedMemberId.in(memberIds)
                .and(blockedMember.member.id.in(memberIds)))
            .fetchFirst();
        return member != null;
    }

    @Override
    public boolean existsBlockedMembers(List<Long> memberIds, List<Long> targetIds) {
        // memberIds 중 targetIds 를 차단한 경우가 있는지 확인
        BlockedMember member = queryFactory.selectFrom(blockedMember)
            .where(blockedMember.blockedMemberId.in(targetIds)
                .and(blockedMember.member.id.in(memberIds)))
            .fetchFirst();
        return member != null;
    }

    @Override
    public boolean existsBlockedMembers(List<Long> memberIds, Long targetId) {
        // memberIds 중 targetId를 차단한 경우가 있는지 확인
        BlockedMember member = queryFactory.selectFrom(blockedMember)
            .where(blockedMember.blockedMemberId.eq(targetId)
                .and(blockedMember.member.id.in(memberIds)))
            .fetchFirst();
        return member != null;
    }
}
