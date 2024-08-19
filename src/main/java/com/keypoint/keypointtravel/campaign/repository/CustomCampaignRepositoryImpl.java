package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCampaignRepositoryImpl implements CustomCampaignRepository {

    private final JPAQueryFactory queryFactory;

    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    @Override
    public boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId) {

        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.member.id.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId))
                .and(memberCampaign.isLeader.isTrue()))
            .fetchFirst();
        return result != null;
    }

    @Override
    public boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId) {
        // campaignId에 해당하는 memberCampaign의 memberId 가 blockedMember에서 fk, blockedMemberId가 emberId인 경우가 존재하면ㄱㄱ
        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .innerJoin(blockedMember).on(memberCampaign.member.id.eq(blockedMember.member.id))
            .where(blockedMember.blockedMemberId.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId)))
            .fetchFirst();
        return result != null;
    }


}
