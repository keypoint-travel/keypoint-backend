package com.keypoint.keypointtravel.place.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryExcelUseCase {

    private String iso2;

    private String nameEN;

    private String nameKO;

    private String nameJA;

    private Double longitude;

    private Double latitude;

    public static CountryExcelUseCase of(
        String iso2,
        String nameEN,
        String nameKO,
        String nameJA,
        Double longitude,
        Double latitude
    ) {
        return new CountryExcelUseCase(
            iso2,
            nameEN,
            nameKO,
            nameJA,
            longitude,
            latitude
        );
    }

    public void setLocation(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Country toCountryEntity() {
        return Country.builder()
            .countryEN(this.nameEN)
            .countryKO(this.nameKO)
            .countryJA(this.nameJA)
            .iso2(iso2)
            .build();
    }

    public Place toPlaceEntity(Country country) {
        return Place.builder()
            .country(country)
            .longitude(this.longitude)
            .latitude(this.latitude)
            .placeType(PlaceType.COUNTRY)
            .build();
    }
}
