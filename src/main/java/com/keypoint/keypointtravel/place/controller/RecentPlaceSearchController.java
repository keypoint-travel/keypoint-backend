package com.keypoint.keypointtravel.place.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.place.dto.request.CreateRecentPlaceSearchRequest;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.response.ReadRecentPlaceSearchResponse;
import com.keypoint.keypointtravel.place.dto.useCase.DeleteRecentPlaceSearchUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.PlaceIdUseCase;
import com.keypoint.keypointtravel.place.redis.service.RecentPlaceSearchService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class RecentPlaceSearchController {

    private final RecentPlaceSearchService recentPlaceSearchService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/search-history")
    public APIResponseEntity<PlaceResponse> getPlaces(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody CreateRecentPlaceSearchRequest request
    ) {
        PlaceIdUseCase useCase = PlaceIdUseCase.of(
            userDetails.getId(), request.getPlaceId()
        );
        recentPlaceSearchService.addSearchWord(useCase);

        return APIResponseEntity.<PlaceResponse>builder()
            .message("장소 검색 기록 추가 성공")
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("/search-history")
    public APIResponseEntity<Void> getPlaces(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "place-search-word-id") String placeSearchWordId
    ) {
        DeleteRecentPlaceSearchUseCase useCase = DeleteRecentPlaceSearchUseCase.of(
            userDetails.getId(),
            placeSearchWordId
        );
        recentPlaceSearchService.deleteSearchWord(useCase);
        return APIResponseEntity.<Void>builder()
            .message("장소 검색 기록 삭제 성공")
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/search-history")
    public APIResponseEntity<List<ReadRecentPlaceSearchResponse>> getPlaceHistoryWords(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        List<ReadRecentPlaceSearchResponse> result = recentPlaceSearchService.getPlaceHistoryWords(
            useCase
        );

        return APIResponseEntity.<List<ReadRecentPlaceSearchResponse>>builder()
            .message("장소 검색 기록 조회 성공")
            .data(result)
            .build();
    }
}
