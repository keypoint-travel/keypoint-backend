package com.keypoint.keypointtravel.visitedCountry.repository;

import com.keypoint.keypointtravel.campaign.entity.QCampaign;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.campaign.entity.QTravelLocation;
import com.keypoint.keypointtravel.global.dto.useCase.SearchPageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.place.entity.QPlace;
import com.keypoint.keypointtravel.visitedCountry.dto.dto.VisitedCountryWithSequenceDTO;
import com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse.SearchCampaignResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VisitedCountryRepository {
    private final JPAQueryFactory queryFactory;
    private final QCampaign campaign = QCampaign.campaign;
    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;
    private final QPlace place = QPlace.place;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;
    private final QTravelLocation travelLocation = QTravelLocation.travelLocation;

    public Page<SearchCampaignResponse> findCampaignsByKeyword(
            Long memberId,
            LanguageCode languageCode,
        SearchPageAndMemberIdUseCase useCase
    ) {
        String keyword = useCase.getKeyword();
        Pageable pageable = useCase.getPageable();
        String direction = useCase.getDirection();
        String sortBy = useCase.getSortBy();

        BooleanBuilder memberCampaignBuilder = new BooleanBuilder();
        memberCampaignBuilder.and(memberCampaign.campaign.id.eq(campaign.id))
            .and(memberCampaign.member.id.eq(memberId));

        List<Long> campaignId = new ArrayList<>();
        long count = 0;
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, campaign.title);
        if (keyword != null && !keyword.isBlank()) {
            // 1. 검색어가 존재하는 경우
            // 1. 키워드에 해당하는 placeId를 찾아서 해당 placeId를 포함하는 캠페인만 조회
        } else {
            // 2. 검색어가 존재하지 않는 경우
            // 2-1 정렬 기준 추가
            if (sortBy != null) {
                Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

                switch (sortBy) {
                    case "title":
                        orderSpecifier = new OrderSpecifier<>(order, campaign.title);
                        break;
                    case "startAt":
                        orderSpecifier = new OrderSpecifier<>(order, campaign.startDate);
                        break;
                }
            }

            // 2-2. campaignId, count 구하기
            campaignId = queryFactory.select(campaign.id)
                .from(campaign)
                .innerJoin(memberCampaign).on(memberCampaignBuilder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

            count = queryFactory.select(campaign.count())
                .from(campaign)
                .innerJoin(memberCampaign).on(memberCampaignBuilder)
                .fetchOne();
        }

        List<SearchCampaignResponse> result = queryFactory
            .selectFrom(campaign)
            .innerJoin(uploadFile).on(uploadFile.id.eq(campaign.campaignImageId))
            .innerJoin(memberDetail).on(memberDetail.member.id.eq(memberId))
            .innerJoin(travelLocation).on(travelLocation.campaign.id.eq(campaign.id))
            .leftJoin(place).on(place.id.eq(travelLocation.placeId))
            .orderBy(orderSpecifier)
            .where(campaign.id.in(campaignId))
            .transform(
                GroupBy.groupBy(campaign.id).list(
                    Projections.constructor(
                        SearchCampaignResponse.class,
                        campaign.id,
                        campaign.title,
                        campaign.startDate,
                        campaign.endDate,
                        uploadFile.path,
                        memberDetail.language,
                        GroupBy.list(
                            Projections.fields(
                                VisitedCountryWithSequenceDTO.class,
                                place.id.as("placeId"),
                                travelLocation.sequence,
                                place.as("place")
                            )
                        )
                    )
                )
            );

        return new PageImpl<>(result, pageable, count);
    }
}
