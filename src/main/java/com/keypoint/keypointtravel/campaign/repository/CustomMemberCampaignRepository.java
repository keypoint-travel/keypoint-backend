package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.QCampaignWaitMember;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomMemberCampaignRepository {

    private final JPAQueryFactory queryFactory;

    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    private final QCampaignWaitMember campaignWaitMember = QCampaignWaitMember.campaignWaitMember;

    public boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId) {
        // 캠페인 리더가 맞는지, 캠페인이 존재하는지 확인
        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.member.id.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId))
                .and(memberCampaign.isLeader.isTrue()))
            .fetchFirst();
        return result != null;
    }

    public boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId) {
        // campaignId에 해당하는 캠페인에 참여 하는 인원들 중 memberId에 해당하는 사용자를 차단 여부 확인
        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .innerJoin(blockedMember).on(memberCampaign.member.id.eq(blockedMember.member.id))
            .where(blockedMember.blockedMemberId.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId)))
            .fetchFirst();
        return result != null;
    }

    public MemberCampaign findCampaignLeader(Long campaignId) {
        // campaignId에 해당하는 leader memberCampaign 찾기
        MemberCampaign memberCampaign = queryFactory.selectFrom(this.memberCampaign)
            .where(this.memberCampaign.campaign.id.eq(campaignId)
                .and(this.memberCampaign.isLeader.isTrue())
                .and(this.memberCampaign.campaign.status.eq(Status.IN_PROGRESS)))
            .fetchOne();
        if (memberCampaign == null) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        return memberCampaign;
    }

    public List<MemberCampaign> findMembersByCampaignCode(String campaignCode) {
        return queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.campaign.invitation_code.eq(campaignCode)
                .and(memberCampaign.campaign.status.eq(Status.IN_PROGRESS)))
            .fetch();
    }

    public List<MemberCampaign> findMembersByCampaignCode(Long campaignId) {
        return queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.campaign.id.eq(campaignId)
                .and(memberCampaign.campaign.status.eq(Status.IN_PROGRESS)))
            .fetch();
    }

    public List<MemberInfoDto> findWaitMembers(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(MemberInfoDto.class,
                    memberDetail.member.id,
                    uploadFile.path,
                    memberDetail.name))
            .from(memberDetail)
            .innerJoin(campaignWaitMember).on(campaignWaitMember.member.id.eq(memberDetail.member.id))
            .leftJoin(uploadFile).on(memberDetail.profileImageId.eq(uploadFile.id))
            .where(campaignWaitMember.campaign.id.eq(campaignId))
            .fetch();
    }
}
