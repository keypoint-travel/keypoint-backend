package com.keypoint.keypointtravel.notice.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.notice.dto.request.CreateNoticeContentRequest;
import com.keypoint.keypointtravel.notice.dto.request.CreateNoticeRequest;
import com.keypoint.keypointtravel.notice.dto.request.UpdateNoticeContentRequest;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail.AdminNoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.CreateNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentsUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.PlusNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase;
import com.keypoint.keypointtravel.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponseEntity<Void> saveNotice(
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detail") @Valid CreateNoticeRequest request
    ) {
        //todo: 관리자 인증 로직 추가 예정
        CreateNoticeUseCase useCase = CreateNoticeUseCase.of(thumbnailImage, request);
        noticeService.saveNotice(useCase);
        return APIResponseEntity.<Void>builder()
            .message("공지등록 성공")
            .build();
    }

    /**
     * 이미 생성된 공지사항에 다른 언어로 추가
     *
     * @param noticeId
     * @param request
     * @return
     */
    @PostMapping("/{noticeId}/translations")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponseEntity<Void> saveNotice(
        @PathVariable(value = "noticeId", required = false) Long noticeId,
        @RequestBody @Valid CreateNoticeContentRequest request
    ) {
        //todo: 관리자 인증 로직 추가 예정
        PlusNoticeUseCase useCase = PlusNoticeUseCase.of(noticeId, request);
        noticeService.saveNoticeByOtherLanguage(useCase);
        return APIResponseEntity.<Void>builder()
            .message("추가 공지등록 성공")
            .build();
    }

    /**
     * @param sortBy id
     * @param direction asc, desc
     * @param pageable
     * @return
     */
    @GetMapping("/management")
    public APIResponseEntity<PageResponse<NoticeResponse>> findNoticesInWeb(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(sort = "modifyAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        PageAndMemberIdUseCase useCase = PageAndMemberIdUseCase.of(
            null,
            sortBy,
            direction,
            pageable
        );
        Page<NoticeResponse> result = noticeService.findNoticesInWeb(useCase);

        return APIResponseEntity.toPage(
            "공지사항 목록 조회 성공",
            result
        );
    }

    /**
     * @param sortBy    id
     * @param direction asc, desc
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public APIResponseEntity<PageResponse<NoticeResponse>> findNoticesInApp(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(sort = "modifyAt", direction = Sort.Direction.ASC) Pageable pageable
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
        Page<NoticeResponse> result = noticeService.findNoticesInApp(useCase);

        return APIResponseEntity.toPage(
            "공지사항 목록 조회 성공",
            result
        );
    }

    @PutMapping("/{noticeId}")
    public APIResponseEntity<Void> updateNotice(
        @PathVariable("noticeId") Long noticeId,
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage) {
        //todo: 관리자 인증 로직 추가 예정
        UpdateNoticeUseCase useCase = UpdateNoticeUseCase.of(
            noticeId,
            thumbnailImage
        );
        noticeService.updateNotice(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 수정 성공")
            .build();
    }

    @PutMapping("/{noticeId}/translations/{noticeContentId}")
    public APIResponseEntity<Void> updateNoticeContent(
        @PathVariable("noticeId") Long noticeId,
        @PathVariable("noticeContentId") Long noticeContentId,
        @RequestBody @Valid UpdateNoticeContentRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        UpdateNoticeContentUseCase useCase = UpdateNoticeContentUseCase.of(
            noticeId,
            noticeContentId,
            request
        );
        noticeService.updateNoticeContent(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 수정 성공")
            .build();
    }

    @Deprecated
    @DeleteMapping()
    public APIResponseEntity<Void> deleteNotices(
        @RequestParam("notice-ids") Long[] noticeIds
    ) {
        DeleteNoticeUseCase useCase = DeleteNoticeUseCase.from(
            noticeIds
        );
        noticeService.deleteNotices(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 삭제 성공")
            .build();
    }

    @Deprecated
    @DeleteMapping("/contents")
    public APIResponseEntity<Void> deleteNoticeContents(
        @RequestParam("notice-content-ids") Long[] noticeContentIds
    ) {
        DeleteNoticeContentsUseCase useCase = DeleteNoticeContentsUseCase.from(
            noticeContentIds
        );
        noticeService.deleteNoticeContents(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 내용 삭제 성공")
            .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{noticeId}/translations/{noticeContentId}")
    public APIResponseEntity<Void> deleteNoticeContent(
        @PathVariable(value = "noticeId") Long noticeId,
        @PathVariable(value = "noticeContentId") Long noticeContentId
    ) {
        DeleteNoticeContentUseCase useCase = DeleteNoticeContentUseCase.of(
            noticeId,
            noticeContentId
        );
        noticeService.deleteNoticeContent(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 내용 삭제 성공")
            .build();
    }


    @GetMapping("/management/{noticeContentId}")
    public APIResponseEntity<AdminNoticeDetailResponse> findNoticeById(
        @PathVariable("noticeContentId") Long noticeId
    ) {
        AdminNoticeDetailResponse result = noticeService.findNoticeById(noticeId);

        return APIResponseEntity.<AdminNoticeDetailResponse>builder()
            .message("공지 단건 조회 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{noticeId}")
    public APIResponseEntity<NoticeDetailResponse> findNoticeByNoticeContentId(
        @PathVariable("noticeId") Long noticeContentId
    ) {
        NoticeDetailResponse result = noticeService.findNoticeByNoticeContentId(noticeContentId);

        return APIResponseEntity.<NoticeDetailResponse>builder()
            .message("공지 단건 조회 성공")
            .data(result)
            .build();
    }
}
