package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase.CountryDetailUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "countryAPIClient",
    url = "https://countriesnow.space/"
)
public interface CountryAPIService {

    @GetMapping("api/v0.1/countries/positions")
    CountryDetailUseCase getCountryDetails();
}



