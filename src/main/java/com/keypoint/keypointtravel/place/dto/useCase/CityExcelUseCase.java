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

    private String nameEN;

    private String nameKO;

    private String nameJA;

    private Double longitude;

    private Double latitude;

    public static CityExcelUseCase of(
        String iso2,
        String nameEN,
        String nameKO,
        String nameJA,
        Double longitude,
        Double latitude
    ) {
        return new CityExcelUseCase(
            iso2,
            nameEN,
            nameKO,
            nameJA,
            longitude,
            latitude
        );
    }

    public Place toEntity(Country country) {
        return Place.builder()
            .country(country)
            .cityEN(this.nameEN)
            .cityKO(this.nameKO)
            .cityJA(this.nameJA)
            .longitude(this.longitude)
            .latitude(this.latitude)
            .placeType(PlaceType.CITY)
            .build();
    }

}
