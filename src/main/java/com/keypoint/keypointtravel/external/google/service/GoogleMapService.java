package com.keypoint.keypointtravel.external.google.service;

import com.keypoint.keypointtravel.external.google.dto.geocoding.GeocodingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleMapService {

    @Value("${google.api-key}")
    private String googleAPIKey;

    private final GoogleMapAPIService googleMapAPIService;

    /**
     * 주소에 대한 경도, 위도 정보를 얻는 함수
     *
     * @param address 검색할 주소
     * @return
     */
    public GeocodingUseCase getGeocodingUseCase(String address) {
        return googleMapAPIService.getGeocodingResult(
            address,
            googleAPIKey
        );
    }
}
