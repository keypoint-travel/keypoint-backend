package com.keypoint.keypointtravel.requestPush.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushRequest;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestPushUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushUseCase;
import com.keypoint.keypointtravel.requestPush.service.RequestPushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
