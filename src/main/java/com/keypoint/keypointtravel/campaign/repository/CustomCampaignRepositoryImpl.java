package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.QCampaign;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomCampaignRepositoryImpl implements CustomCampaignRepository {

    private final JPAQueryFactory queryFactory;

    private final QCampaign campaign = QCampaign.campaign;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;

    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId) {
        // 캠페인 리더가 맞는지, 캠페인이 존재하는지 확인
        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.member.id.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId))
                .and(memberCampaign.isLeader.isTrue()))
            .fetchFirst();
        return result != null;
    }

    @Override
    public boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId) {
        // campaignId에 해당하는 캠페인에 참여 하는 인원들 중 memberId에 해당하는 사용자를 차단 여부 확인
        MemberCampaign result = queryFactory.selectFrom(memberCampaign)
            .innerJoin(blockedMember).on(memberCampaign.member.id.eq(blockedMember.member.id))
            .where(blockedMember.blockedMemberId.eq(memberId)
                .and(memberCampaign.campaign.id.eq(campaignId)))
            .fetchFirst();
        return result != null;
    }

    @Override
    public SendInvitationEmailDto findSendInvitationEmailInfo(Long campaignId) {
        // 캠페인 이메일 초대에 필요한 캠페인 리더의 이름, 캠페인 제목, 초대 코드 조회
        SendInvitationEmailDto dto = queryFactory.select(
                Projections.constructor(SendInvitationEmailDto.class,
                    memberDetail.name,
                    campaign.title,
                    campaign.invitation_code))
            .from(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .innerJoin(memberDetail).on(memberCampaign.member.id.eq(memberDetail.member.id))
            .where(campaign.id.eq(campaignId)
                .and(memberCampaign.isLeader.isTrue())).fetchOne();
        if (dto == null) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        return dto;
    }

    @Override
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

    @Override
    public List<MemberCampaign> findMembersByCampaignCode(String campaignCode) {
        return queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.campaign.invitation_code.eq(campaignCode)
                .and(memberCampaign.campaign.status.eq(Status.IN_PROGRESS)))
            .fetch();
    }

    @Override
    public List<MemberCampaign> findMembersByCampaignCode(Long campaignId) {
        return queryFactory.selectFrom(memberCampaign)
            .where(memberCampaign.campaign.id.eq(campaignId)
                .and(memberCampaign.campaign.status.eq(Status.IN_PROGRESS)))
            .fetch();
    }

    @Override
    public CampaignInfoDto findCampaignInfo(Long campaignId) {
        CampaignInfoDto result = queryFactory.select(
                Projections.constructor(CampaignInfoDto.class,
                    campaign.id,
                    uploadFile.path,
                    campaign.title,
                    campaign.startDate,
                    campaign.endDate))
            .from(campaign)
            .leftJoin(uploadFile).on(campaign.campaignImageId.eq(uploadFile.id))
            .where(campaign.id.eq(campaignId))
            .fetchOne();
        if (result == null) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        return result;
    }
}
