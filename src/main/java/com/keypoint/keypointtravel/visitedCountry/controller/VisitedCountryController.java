package com.keypoint.keypointtravel.visitedCountry.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse.SearchVisitedCountryResponse;
import com.keypoint.keypointtravel.visitedCountry.service.VisitedCountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visited-countries")
public class VisitedCountryController {
    private final VisitedCountryService visitedCountryService;

    @GetMapping("/campaigns")
    public APIResponseEntity<SearchVisitedCountryResponse> findVisitedCountriesAndCampaigns(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "sort-by", required = false) String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @PageableDefault(size = 15, sort = "order", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        PageAndMemberIdUseCase useCase = PageAndMemberIdUseCase.of(
                userDetails.getId(),
                sortBy,
                direction,
                pageable
        );

        SearchVisitedCountryResponse result = visitedCountryService.findVisitedCountriesAndCampaigns(useCase);

        return APIResponseEntity.<SearchVisitedCountryResponse>builder()
                .message("방문 국가 검색 성공")
                .data(result)
                .build();
    }
}
