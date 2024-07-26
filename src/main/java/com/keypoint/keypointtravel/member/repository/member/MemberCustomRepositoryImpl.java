package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberAlarmResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;

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
}
