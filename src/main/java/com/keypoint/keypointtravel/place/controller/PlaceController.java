package com.keypoint.keypointtravel.place.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.place.dto.response.PlaceResponse;
import com.keypoint.keypointtravel.place.dto.useCase.PlaceSearchUseCase;
import com.keypoint.keypointtravel.place.service.PlaceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public APIResponseEntity<List<PlaceResponse>> getPlaces(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "search-word", required = false) String searchWord
    ) {
        PlaceSearchUseCase useCase = PlaceSearchUseCase.of(userDetails.getId(), searchWord);
        List<PlaceResponse> result = placeService.getPlacesBySearchWord(useCase);

        return APIResponseEntity.<List<PlaceResponse>>builder()
            .message("장소 검색 성공")
            .data(result)
            .build();
    }
}
