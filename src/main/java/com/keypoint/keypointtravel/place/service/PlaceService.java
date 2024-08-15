package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.place.dto.useCase.CityExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import com.keypoint.keypointtravel.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

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
}
