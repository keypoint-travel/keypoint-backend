package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignDto;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.entity.QCampaign;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.QTravelLocation;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.place.entity.QCountry;
import com.keypoint.keypointtravel.place.entity.QPlace;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.querydsl.jpa.JPAExpressions.select;

import java.sql.Date;
import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

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
                    memberDetail.member.name,
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
    public CampaignDto findCampaignInfoList(
        Long memberId, Status status, int size, int page) {

        List<CampaignInfoDto> dtoList = queryFactory.select(
                Projections.constructor(CampaignInfoDto.class,
                    campaign.id,
                    uploadFile.path,
                    campaign.title,
                    campaign.startDate,
                    campaign.endDate))
            .from(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .leftJoin(uploadFile).on(campaign.campaignImageId.eq(uploadFile.id))
            .where(campaign.status.eq(status)
                .and(memberCampaign.member.id.eq(memberId)))
            .orderBy(campaign.endDate.desc())
            .offset((long) (size > 0 ? size : 1) * (page > 0 ? page - 1 : 0))
            .limit((size > 0 ? size : 1))
            .fetch();
        Long count = queryFactory.select(campaign.count())
            .from(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .where(campaign.status.eq(status)
                .and(memberCampaign.member.id.eq(memberId)))
            .fetchOne();
        return new CampaignDto(dtoList, count);
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
        Locale locale = LocaleContextHolder.getLocale();
        QPlace place = QPlace.place;
        QCountry country = QCountry.country;
        QTravelLocation travelLocation = QTravelLocation.travelLocation;
        return queryFactory.select(
                Projections.constructor(TravelLocationDto.class,
                    travelLocation.sequence,
                    travelLocation.placeId,
                    locale.getLanguage().equals("ko") ? place.cityKO :
                        locale.getLanguage().equals("en") ? place.cityEN : place.cityJA,
                    locale.getLanguage().equals("ko") ? country.countryKO :
                        locale.getLanguage().equals("en") ? country.countryEN : country.countryJA))
            .from(travelLocation)
            .innerJoin(place).on(travelLocation.placeId.eq(place.id))
            .innerJoin(country).on(place.country.id.eq(country.id))
            .where(travelLocation.campaign.id.eq(campaignId))
            .fetch();
    }

    @Override
    public boolean existsOverlappingCampaign(List<Long> memberIds, Date startDate, Date endDate) {
        // startDate, endDate 사이 기간에 해당하는 캠페인이 있는지 조회
        List<Campaign> campaigns = queryFactory.selectFrom(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .where(memberCampaign.member.id.in(memberIds)
                .and(campaign.startDate.loe(endDate).and(campaign.endDate.goe(startDate))))
            .fetch();
        return !campaigns.isEmpty();
    }

    @Override
    public boolean existsOverlappingCampaign(List<Long> memberIds, Date startDate, Date endDate, Long campaignId) {
        // startDate, endDate 사이 기간에 해당하는 캠페인이 있는지 조회
        List<Campaign> campaigns = queryFactory.selectFrom(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .where(memberCampaign.member.id.in(memberIds)
                .and(campaign.startDate.loe(endDate).and(campaign.endDate.goe(startDate)))
                .and(campaign.id.ne(campaignId)))
            .fetch();
        return !campaigns.isEmpty();
    }

    @Override
    public boolean existsOverlappingCampaign(List<Long> memberIds, Long campaignId) {
        // startDate, endDate 사이 기간에 해당하는 캠페인이 있는지 조회
        List<Campaign> campaigns = queryFactory.selectFrom(campaign)
            .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
            .where(memberCampaign.member.id.in(memberIds)
                .and(campaign.startDate.loe(
                    select(campaign.endDate)
                        .from(campaign)
                        .where(campaign.id.eq(campaignId)))
                    .and(campaign.endDate.goe(
                        select(campaign.startDate)
                            .from(campaign).
                            where(campaign.id.eq(campaignId)))
                    )))
            .fetch();
        return !campaigns.isEmpty();
    }
}
