package com.keypoint.keypointtravel.requestPush.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushAlarmRequest;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.DeleteRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.service.RequestAlarmServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/request-alarms/push")
@RequiredArgsConstructor
public class RequestPushAlarmController {

    private static final RequestAlarmType REQUEST_ALARM_TYPE = RequestAlarmType.PUSH;
    private final RequestAlarmServiceImpl requestPushAlarmService;

    @PostMapping
    public APIResponseEntity<Void> addRequestPushAlarm(
        @Valid @RequestBody RequestPushAlarmRequest request
    ) {
        CreateRequestAlarmUseCase useCase = CreateRequestAlarmUseCase.of(
            request,
            REQUEST_ALARM_TYPE
        );
        requestPushAlarmService.addRequestAlarm(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 요청 생성 성공")
            .build();
    }

    @PutMapping("/{request-push-id}")
    public APIResponseEntity<Void> updateRequestPushAlarm(
        @PathVariable(value = "request-push-id") Long requestPushId,
        @Valid @RequestBody RequestPushAlarmRequest request
    ) {
        UpdateRequestAlarmUseCase useCase = UpdateRequestAlarmUseCase.of(requestPushId,
            request);
        requestPushAlarmService.updateRequestAlarm(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 요청 수정 성공")
            .build();
    }

    @GetMapping
    public APIResponseEntity<PageResponse<RequestAlarmResponse>> findRequestPushAlarms(
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
        Page<RequestAlarmResponse> result = requestPushAlarmService.findRequestAlarms(
            useCase);

        return APIResponseEntity.toPage(
            "푸시 요청 조회 성공",
            result
        );
    }

    @DeleteMapping
    public APIResponseEntity<Void> deleteRequestPushAlarm(
        @RequestParam("request-ids") Long[] requestIds
    ) {
        DeleteRequestAlarmUseCase useCase = DeleteRequestAlarmUseCase.from(requestIds);
        requestPushAlarmService.deleteRequestAlarm(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 요청 삭제 성공")
            .build();
    }
}
