package com.keypoint.keypointtravel.place.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CityExcelUseCase {

    private String iso2;

    private String name_EN;

    private String name_KO;

    private String name_JP;

    private Double longitude;

    private Double latitude;

    public static CityExcelUseCase of(
        String iso2,
        String name_EN,
        String name_KO,
        String name_JP,
        Double longitude,
        Double latitude
    ) {
        return new CityExcelUseCase(
            iso2,
            name_EN,
            name_KO,
            name_JP,
            longitude,
            latitude
        );
    }

    public Place toEntity(Country country) {
        return Place.builder()
            .country(country)
            .cityEN(this.name_EN)
            .cityKO(this.name_KO)
            .cityJP(this.name_JP)
            .longitude(this.longitude)
            .latitude(this.latitude)
            .placeType(PlaceType.CITY)
            .build();
    }

}
