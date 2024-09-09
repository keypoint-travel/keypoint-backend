package com.keypoint.keypointtravel.visitedCountry.dto.dto;

import com.keypoint.keypointtravel.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitedCountryWithSequenceDTO {

    private Long placeId;
    private Long sequence;
    private Place place;
}
