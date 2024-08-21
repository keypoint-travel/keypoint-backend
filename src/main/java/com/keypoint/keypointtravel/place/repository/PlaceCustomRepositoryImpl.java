package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.response.QPlaceResponse;
import com.keypoint.keypointtravel.place.entity.QPlace;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class PlaceCustomRepositoryImpl implements PlaceCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QPlace place = QPlace.place;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    @Override
    public List<PlaceResponse> getPlacesBySearchWord(
        LanguageCode languageCode,
        Long memberId,
        String searchWord
    ) {
        return queryFactory
            .select(
                new QPlaceResponse(
                    place,
                    memberDetail.language
                )
            )
            .from(place)
            .innerJoin(memberDetail).on(memberDetail.member.id.eq(memberId))
            .where(
                checkIsContainSearchWord(languageCode, searchWord)
            )
            .fetch();
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
