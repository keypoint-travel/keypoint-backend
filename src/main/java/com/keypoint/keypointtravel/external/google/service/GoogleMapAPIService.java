package com.keypoint.keypointtravel.external.google.service;

import com.keypoint.keypointtravel.external.google.dto.geocoding.GeocodingUseCase;
import com.keypoint.keypointtravel.global.constants.GoogleAPIConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleMapApiClient", url = GoogleAPIConstants.MAP_URL, fallback = GoogleMapAPIServiceFallback.class)
public interface GoogleMapAPIService {

    @GetMapping(GoogleAPIConstants.GET_GEOCODE)
    GeocodingUseCase getGeocodingResult(
        @RequestParam("address") String address,
        @RequestParam("key") String key
    );
}
