package com.keypoint.keypointtravel.notice.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.request.CreateNoticeRequest;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.CreateNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.PlusNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase;
import com.keypoint.keypointtravel.notice.service.NoticeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public APIResponseEntity<Void> saveNotice(
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detail") @Valid CreateNoticeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        CreateNoticeUseCase useCase = CreateNoticeUseCase.of(thumbnailImage, request);
        noticeService.saveNotice(useCase);
        return APIResponseEntity.<Void>builder()
            .message("공지등록 성공")
            .build();
    }

    // 이미 생성된 공지사항에 다른 언어로 추가
    @PostMapping("/{noticeId}")
    public APIResponseEntity<Void> saveNotice(
        @PathVariable(value = "noticeId", required = false) Long noticeId,
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
        @RequestPart(value = "detail") @Valid CreateNoticeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        PlusNoticeUseCase useCase = new PlusNoticeUseCase(noticeId, thumbnailImage, detailImages, findLanguageValue(request.getLanguage()), request.getTitle(),request.getContent());
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
    @GetMapping
    public APIResponseEntity<PageResponse<NoticeResponse>> findNotices(
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
        Page<NoticeResponse> result = noticeService.findNotices(useCase);

        return APIResponseEntity.toPage(
            "공지사항 목록 조회 성공",
            result
        );
    }

    @PutMapping("/{noticeContentId}")
    public APIResponseEntity<Void> updateNotice(
        @PathVariable("noticeContentId") Long noticeContentId,
        @RequestPart(value = "detail") @Valid UpdateNoticeUseCase request,
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        UpdateNoticeUseCase useCase = UpdateNoticeUseCase.of(
            noticeContentId,
            thumbnailImage,
            detailImages,
            request.getTitle(),
            request.getContent()
        );
        noticeService.updateNotice(useCase);

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
        @RequestParam("noticeContent-ids") Long[] noticeContentIds
    ) {
        DeleteNoticeContentUseCase useCase = DeleteNoticeContentUseCase.from(
            noticeContentIds
        );
        noticeService.deleteNoticeContents(useCase);

        return APIResponseEntity.<Void>builder()
            .message("공지 내용 삭제 성공")
            .build();
    }

    @GetMapping("/{noticeContentId}")
    public APIResponseEntity<NoticeDetailResponse> findNoticeById(
        @PathVariable("noticeContentId") Long noticeContentId
    ) {
        NoticeDetailResponse result = noticeService.findNoticeById(noticeContentId);

        return APIResponseEntity.<NoticeDetailResponse>builder()
            .message("공지 단건 조회 성공")
            .data(result)
            .build();
    }


    private static LanguageCode findLanguageValue(String language) {
        if (language.equals("ko")) {
            return LanguageCode.KO;
        }
        if (language.equals("en")) {
            return LanguageCode.EN;
        }
        if (language.equals("ja")) {
            return LanguageCode.JA;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }


}
