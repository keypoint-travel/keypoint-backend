package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.member.dto.response.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberAlarmResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.querydsl.jpa.JPAExpressions.selectOne;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    @Override
    public MemberProfileResponse findMemberProfile(Long memberId) {
        return queryFactory
            .select(
                Projections.fields(
                    MemberProfileResponse.class,
                    member.memberDetail.name,
                    member.email,
                    uploadFile.path.as("profileImageUrl"),
                    Projections.fields(
                        MemberAlarmResponse.class,
                        member.notification.pushNotificationEnabled,
                        member.notification.marketingNotificationEnabled
                    ).as("alarms")
                )
            )
            .from(member)
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .where(member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    public OtherMemberProfileResponse findOtherMemberProfile(Long myId, Long otherMemberId) {
        return queryFactory.select(
                Projections.constructor(
                    OtherMemberProfileResponse.class,
                    member.id.as("memberId"),
                    uploadFile.path.as("profileImageUrl"),
                    member.memberDetail.name.as("memberName"),
                    isBlocked(myId, otherMemberId).as("isBlocked")
                )
            )
            .from(member)
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .where(member.id.eq(otherMemberId))
            .fetchOne();
    }

    private BooleanExpression isBlocked(Long myId, Long otherMemberId) {
        // 로그인 하지 않았을 경우
        if (myId == null) {
            return Expressions.FALSE;
        }
        // 로그인 하였을 경우 : 차단 회원 엔티티에서 차단 여부 확인
        return selectOne()
            .from(QBlockedMember.blockedMember)
            .where(blockedMember.member.id.eq(myId)
                .and(blockedMember.blockedMemberId.eq(otherMemberId)))
            .exists();
    }
}