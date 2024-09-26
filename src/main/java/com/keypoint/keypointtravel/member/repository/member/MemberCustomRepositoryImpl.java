package com.keypoint.keypointtravel.member.repository.member;

import static com.querydsl.jpa.JPAExpressions.selectOne;

import com.keypoint.keypointtravel.badge.entity.QEarnedBadge;
import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberAlarmResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.premium.entity.QMemberPremium;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;
    private final QEarnedBadge earnedBadge = QEarnedBadge.earnedBadge;
    private final QMemberPremium memberPremium = QMemberPremium.memberPremium;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;

    @Override
    public MemberProfileResponse findMemberProfile(Long memberId) {
        QUploadFile badgeFile = new QUploadFile("badgeFile");

        return queryFactory
            .select(
                Projections.constructor(
                    MemberProfileResponse.class,
                    member.id,
                    memberDetail.name,
                    member.email,
                    uploadFile.path,
                    memberDetail.language,
                    Projections.fields(
                        MemberAlarmResponse.class,
                        member.notification.pushNotificationEnabled,
                        member.notification.marketingNotificationEnabled
                    ),
                    earnedBadge.count(),
                    memberCampaign.count(),
                    badgeFile.path,
                    memberPremium.count()
                )
            )
            .from(member)
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .innerJoin(member.memberDetail, memberDetail)
            .leftJoin(badgeFile).on(memberDetail.representativeBadge.id.eq(badgeFile.id))
            .leftJoin(memberPremium).on(memberPremium.member.id.eq(memberId))
            .leftJoin(earnedBadge).on(earnedBadge.member.id.eq(memberId))
            .leftJoin(memberCampaign).on(memberCampaign.member.id.eq(memberId))
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

    @Override
    public MemberSettingResponse findSettingByMemberId(Long memberId) {
        return queryFactory
            .select(
                Projections.fields(
                    MemberSettingResponse.class,
                    member.memberDetail.language,
                    member.notification.pushNotificationEnabled
                )
            )
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    public List<MemberInfoDto> findCampaignMemberList(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(
                    MemberInfoDto.class,
                    member.id,
                    uploadFile.path,
                    member.memberDetail.name))
            .from(member)
            .innerJoin(memberCampaign).on(memberCampaign.member.id.eq(member.id))
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .where(memberCampaign.campaign.id.eq(campaignId))
            .fetch();
    }

    private BooleanExpression isBlocked(Long myId, Long otherMemberId) {
        return selectOne()
            .from(QBlockedMember.blockedMember)
            .where(blockedMember.member.id.eq(myId)
                .and(blockedMember.blockedMemberId.eq(otherMemberId)))
            .exists();
    }
}