package com.keypoint.keypointtravel.place.dto.useCase.cityByCountryUseCase;

import java.util.List;
import lombok.Getter;

@Getter
public class CityByCountryUseCase {

    private boolean error;

    private String msg;

    private List<CityByCountryContentUseCase> data;
}
