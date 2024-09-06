package com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse;

import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchVisitedCountryResponse {
    private List<SearchPlaceResponse> visitedCountries;
    private PageResponse<SearchCampaignResponse> campaigns;
}
