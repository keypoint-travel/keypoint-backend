package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.response.ReadRecentPlaceSearchResponse;
import com.keypoint.keypointtravel.place.redis.entity.RecentPlaceSearch;
import java.util.List;

public interface PlaceCustomRepository {

    List<PlaceResponse> getRecentSearchPlaces(
        LanguageCode languageCode,
        Long memberId,
        String searchWord
    );

    List<ReadRecentPlaceSearchResponse> getRecentSearchPlaces(
        List<RecentPlaceSearch> recentPlaceSearches,
        Long memberId
    );
}
