package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import java.util.List;

public interface PlaceCustomRepository {

    List<PlaceResponse> getPlacesBySearchWord(
        LanguageCode languageCode,
        Long memberId,
        String searchWord
    );
}
