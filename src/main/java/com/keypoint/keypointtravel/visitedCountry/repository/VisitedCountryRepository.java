package com.keypoint.keypointtravel.visitedCountry.repository;

import com.keypoint.keypointtravel.campaign.entity.QCampaign;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.place.entity.QPlace;
import com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse.SearchCampaignResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VisitedCountryRepository {
    private final JPAQueryFactory queryFactory;
    private final QCampaign campaign = QCampaign.campaign;
    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;
    private final QPlace place = QPlace.place;

    public Page<SearchCampaignResponse> findCampaignsByKeyword(
            Long memberId,
            LanguageCode languageCode,
            PageAndMemberIdUseCase useCase
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(memberCampaign.member.id.eq(memberId));

        // 1. 키워드에 해당하는 placeId를 찾아서 해당 placeId를 포함하는 캠페인만 조회


        queryFactory
                .select()
                .from(campaign)
                .innerJoin(memberCampaign).on(campaign.id.eq(memberCampaign.campaign.id))
                .where(builder);
        return null;
    }

    private BooleanBuilder checkIsContainSearchWord(LanguageCode languageCode, String searchWord) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchWord != null) {
            switch (languageCode) {
                case EN:
                    builder.or(place.cityEN.isNotNull().and(place.cityEN.contains(searchWord)))
                            .or(place.country.countryEN.contains(searchWord));
                    break;
                case KO:
                    builder.or(place.cityKO.isNotNull().and(place.cityKO.contains(searchWord)))
                            .or(place.country.countryKO.contains(searchWord));
                    break;
                case JA:
                    builder.or(place.cityJA.isNotNull().and(place.cityJA.contains(searchWord)))
                            .or(place.country.countryJA.contains(searchWord));
                    break;
            }
        }

        return builder;
    }
}
