package com.keypoint.keypointtravel.friend.repository;

import com.keypoint.keypointtravel.friend.entity.QFriend;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendCustomRepositoryImpl implements FriendCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QFriend friend = QFriend.friend;

    private final QMember member = QMember.member;

    @Override
    public Member findMemberByEmailOrInvitationCode(String invitationValue) {
        Member friend = queryFactory.select(member)
            .from(member)
            .where(eqEmailOrInvitationCode(invitationValue))
            .fetchOne();
        // 친구 코드 혹은 이메일에 해당하는 회원이 없을 시 예외 처리
        if (friend == null) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL_OR_INVITATION_CODE);
        }
        return friend;
    }

    private BooleanExpression eqEmailOrInvitationCode(String invitationValue) {
        return invitationValue.contains("@") ? member.email.eq(invitationValue) :
            member.invitationCode.eq(invitationValue);
    }
}
