package com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import com.keypoint.keypointtravel.visitedCountry.dto.dto.VisitedCountryWithSequenceDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchCampaignResponse {

    private Long campaignId;
    private String title;
    private Date startAt;
    private Date endAt;
    private String coverImageUrl;
    private List<String> places;
    private List<Long> placeIds = new ArrayList<>();

    @QueryProjection
    public SearchCampaignResponse(
        Long campaignId,
        String title,
        Date startAt,
        Date endAt,
        String coverImageUrl,
        LanguageCode languageCode,
        List<VisitedCountryWithSequenceDTO> dtos
    ) {
        this.campaignId = campaignId;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.coverImageUrl = coverImageUrl;

        List<String> places = new ArrayList<>();
        for (VisitedCountryWithSequenceDTO dto : dtos) {
            Place place = dto.getPlace();
            if (place.getPlaceType() == PlaceType.COUNTRY) {
                String name = getCountryName(place.getCountry(), languageCode);
                if (name != null && !places.contains(name)) {
                    places.add(name);
                }
            } else {
                String countryName = getCountryName(place.getCountry(), languageCode);
                if (countryName != null && !places.contains(countryName)) {
                    places.add(countryName);
                }

                String cityName = getCityName(place, languageCode);
                if (cityName != null && !places.contains(cityName)) {
                    places.add(cityName);
                }
            }

            this.placeIds.add(dto.getPlaceId());
        }
        this.places = places;
    }

    private String getCountryName(Country country, LanguageCode languageCode) {
        switch (languageCode) {
            case EN:
                return country.getCountryEN();
            case JA:
                return country.getCountryJA();
            case KO:
                return country.getCountryKO();
        }
        return null;
    }

    private String getCityName(Place place, LanguageCode languageCode) {
        switch (languageCode) {
            case EN:
                return place.getCityEN();
            case JA:
                return place.getCityJA();
            case KO:
                return place.getCityKO();
        }
        return null;
    }
}
