package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.entity.*;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.place.entity.QCountry;
import com.keypoint.keypointtravel.place.entity.QPlace;
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

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

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

    @Override
    public List<CampaignInfoDto> findCampaignInfoList(Long memberId) {
        return queryFactory.select(
                Projections.constructor(CampaignInfoDto.class,
                    campaign.id,
                    uploadFile.path,
                    campaign.title,
                    campaign.startDate,
                    campaign.endDate))
            .from(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .leftJoin(uploadFile).on(campaign.campaignImageId.eq(uploadFile.id))
            .where(campaign.status.eq(Status.IN_PROGRESS)
                .and(memberCampaign.member.id.eq(memberId)))
            .fetch();
    }

    @Override
    public void updateCampaignFinished(Long campaignId) {
        queryFactory.update(campaign)
            .set(campaign.status, Status.FINISHED)
            .where(campaign.id.eq(campaignId))
            .execute();
    }

    @Override
    public List<TravelLocationDto> findTravelLocationList(Long campaignId) {
        QPlace place = QPlace.place;
        QCountry country = QCountry.country;
        QTravelLocation travelLocation = QTravelLocation.travelLocation;
        return queryFactory.select(
                Projections.constructor(TravelLocationDto.class,
                    travelLocation.sequence,
                    travelLocation.placeId,
                    place.cityKO,
                    country.countryKO))
            .from(travelLocation)
            .innerJoin(place).on(travelLocation.placeId.eq(place.id))
            .innerJoin(country).on(place.country.id.eq(country.id))
            .where(travelLocation.campaign.id.eq(campaignId))
            .fetch();
    }
}
