package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.response.QPlaceResponse;
import com.keypoint.keypointtravel.place.dto.response.QReadRecentPlaceSearchResponse;
import com.keypoint.keypointtravel.place.dto.response.ReadRecentPlaceSearchResponse;
import com.keypoint.keypointtravel.place.entity.QPlace;
import com.keypoint.keypointtravel.place.redis.entity.RecentPlaceSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class PlaceCustomRepositoryImpl implements PlaceCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QPlace place = QPlace.place;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    @Override
    public List<PlaceResponse> getRecentSearchPlaces(
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

    @Override
    public List<ReadRecentPlaceSearchResponse> getRecentSearchPlaces(
        List<RecentPlaceSearch> recentPlaceSearches,
        Long memberId
    ) {
        // 정렬된 recentPlaceSearches 리스트에서 placeId들을 추출하여 리스트로 변환
        List<Long> placeIds = recentPlaceSearches.stream()
            .map(RecentPlaceSearch::getPlaceId)
            .toList();

        List<ReadRecentPlaceSearchResponse> responses = queryFactory
            .select(
                new QReadRecentPlaceSearchResponse(
                    place,
                    memberDetail.language
                )
            )
            .from(place)
            .innerJoin(memberDetail).on(memberDetail.member.id.eq(memberId))
            .where(
                place.id.in(placeIds)
            )
            .fetch();

        // placeId를 기준으로 RecentPlaceSearch 와 매핑
        Map<Long, RecentPlaceSearch> recentPlaceSearchMap = recentPlaceSearches.stream()
            .collect(Collectors.toMap(RecentPlaceSearch::getPlaceId, Function.identity()));

        // 각 response 에 대해 placeSearchHistoryId를 매핑
        responses.forEach(response -> {
            RecentPlaceSearch recentPlaceSearch = recentPlaceSearchMap.get(response.getPlaceId());
            if (recentPlaceSearch != null) {
                response.setPlaceSearchHistoryId(recentPlaceSearch.getId());
            }
        });

        // 최근 검색한 순으로 정렬
        Map<Long, Integer> sortedPlaceIdIndexMap = new HashMap<>();
        for (int i = 0; i < placeIds.size(); i++) {
            sortedPlaceIdIndexMap.put(placeIds.get(i), i);
        }

        return responses.stream()
            .sorted(Comparator.comparingInt(
                response -> sortedPlaceIdIndexMap.get(response.getPlaceId())))
            .toList();
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
