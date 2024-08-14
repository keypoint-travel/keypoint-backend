package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.place.dto.useCase.cityByCountryUseCase.CityByCountryUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase.CountryDetailUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "countryAPIClient",
    url = "https://countriesnow.space/api/v0.1"
)
public interface CountriesnowService {

    @GetMapping("/countries/positions")
    CountryDetailUseCase getCountryDetails();

    @GetMapping("/countries")
    CityByCountryUseCase getCitiesByCountry();
}



