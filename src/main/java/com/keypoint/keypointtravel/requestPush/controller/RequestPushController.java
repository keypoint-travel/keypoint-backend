package com.keypoint.keypointtravel.requestPush.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushRequest;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestPushUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushUseCase;
import com.keypoint.keypointtravel.requestPush.service.RequestPushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/push-requests")
@RequiredArgsConstructor
public class RequestPushController {

    private final RequestPushService requestPushService;

    @PostMapping
    public APIResponseEntity<Void> addRequestPush(
        @Valid @RequestBody RequestPushRequest request
    ) {
        CreateRequestPushUseCase useCase = CreateRequestPushUseCase.from(request);
        requestPushService.addRequestPush(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 요청 생성 성공")
            .build();
    }

    @PutMapping("/{request-push-id}")
    public APIResponseEntity<Void> updateRequestPush(
        @PathVariable(value = "request-push-id") Long requestPushId,
        @Valid @RequestBody RequestPushRequest request
    ) {
        UpdateRequestPushUseCase useCase = UpdateRequestPushUseCase.of(requestPushId, request);
        requestPushService.updateRequestPush(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 요청 수정 성공")
            .build();
    }

    @GetMapping
    public APIResponseEntity<PageResponse<RequestPushResponse>> findRequestPushes(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(sort = "modifyAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        PageUseCase useCase = PageUseCase.of(
            sortBy,
            direction,
            pageable
        );
        Page<RequestPushResponse> result = requestPushService.findRequestPushes(useCase);

        return APIResponseEntity.toPage(
            "푸시 요청 조회 성공",
            result
        );
    }
}
