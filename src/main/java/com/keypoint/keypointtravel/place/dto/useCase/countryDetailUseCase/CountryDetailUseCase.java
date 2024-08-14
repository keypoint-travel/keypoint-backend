package com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase;

import java.util.List;
import lombok.Getter;

@Getter
public class CountryDetailUseCase {

    private boolean error;

    private String msg;

    private List<CountryDetailContentUseCase> data;
}
