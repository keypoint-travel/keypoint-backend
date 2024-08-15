package com.keypoint.keypointtravel.place.dto.response;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceResponse {

    private Long placeId;
    private String displayName;
    private String cityName;
    private String countryName;
    private PlaceType placeType;

    @QueryProjection
    public PlaceResponse(Place place, LanguageCode languageCode) {
        this.placeId = place.getId();
        this.placeType = place.getPlaceType();

        Country country = place.getCountry();
        switch (languageCode) {
            case EN -> {
                this.cityName = place.getCityEN();
                this.countryName = country.getCountryEN();
            }
            case JA -> {
                this.cityName = place.getCityJA();
                this.countryName = country.getCountryJA();
            }
            case KO -> {
                this.cityName = place.getCityKO();
                this.countryName = country.getCountryKO();
            }
        }

        this.displayName = placeType == PlaceType.COUNTRY ? countryName
            : String.format("%s, %s", cityName, countryName);

    }
}
