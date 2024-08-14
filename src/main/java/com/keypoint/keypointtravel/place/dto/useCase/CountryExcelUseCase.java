package com.keypoint.keypointtravel.place.dto.useCase;

import lombok.Getter;

@Getter
public class CountryExcelUseCase {

    private String countryCode;

    private String name_EN;

    private String name_KO;

    private String name_JP;

    private Double longitude;

    private Double latitude;

    public CountryExcelUseCase(
        String countryCode,
        String name_EN,
        String name_KO,
        String name_JP
    ) {
        this.countryCode = countryCode;
        this.name_EN = name_EN;
        this.name_KO = name_KO;
        this.name_JP = name_JP;
    }

    public static CountryExcelUseCase of(
        String countryCode,
        String name_EN,
        String name_KO,
        String name_JP
    ) {
        return new CountryExcelUseCase(
            countryCode,
            name_EN,
            name_KO,
            name_JP
        );
    }

    public void setLocation(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
