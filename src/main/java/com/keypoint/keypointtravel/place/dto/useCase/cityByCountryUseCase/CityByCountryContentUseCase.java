package com.keypoint.keypointtravel.place.dto.useCase.cityByCountryUseCase;

import java.util.List;
import lombok.Getter;

@Getter
public class CityByCountryContentUseCase {

    private String iso2;
    private String iso3;
    private String country;
    private List<String> cities;
}
