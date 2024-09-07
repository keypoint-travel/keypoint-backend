package com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse;

import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class SearchVisitedCountryResponse {
    private List<SearchPlaceResponse> visitedCountries;
    private PageResponse<SearchCampaignResponse> campaigns;

    public static SearchVisitedCountryResponse of(
        List<SearchPlaceResponse> visitedCountries,
        Page<SearchCampaignResponse> campaigns
    ) {
        PageResponse<SearchCampaignResponse> pageResponse = PageResponse.<SearchCampaignResponse>builder()
            .content(campaigns.getContent())
            .total(campaigns.getTotalElements())
            .build();

        return new SearchVisitedCountryResponse(
            visitedCountries,
            pageResponse
        );
    }
}
