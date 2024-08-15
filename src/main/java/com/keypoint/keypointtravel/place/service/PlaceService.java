package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.useCase.CityExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.PlaceSearchUseCase;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import com.keypoint.keypointtravel.place.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MemberDetailRepository memberDetailRepository;

    /**
     * Country 타입으로 Place 생성
     *
     * @param country
     * @param useCase
     * @return
     */
    public void addPlaceForCountry(Country country, CountryExcelUseCase useCase) {
        Place place = useCase.toPlaceEntity(country);
        placeRepository.save(place);
    }

    /**
     * City 타입으로 Place 생성
     *
     * @param country
     * @param useCase
     * @return
     */
    @Transactional
    public void addPlaceForCity(Country country, CityExcelUseCase useCase) {
        Place place = useCase.toEntity(country);
        placeRepository.save(place);
    }

    /**
     * 전체 Place 수 반환
     *
     * @return
     */
    public long countAllPlaces() {
        return placeRepository.count();
    }

    /**
     * 검색어에 대해서 장소를 조회하는 함수 - 만약 검색어가 존재하지 않은 경우, 전체 조회
     *
     * @param useCase
     * @return
     */
    public List<PlaceResponse> getPlacesBySearchWord(PlaceSearchUseCase useCase) {
        try {
            LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId());
            return placeRepository.getPlacesBySearchWord(languageCode, useCase.getMemberId(),
                useCase.getSearchWord());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
