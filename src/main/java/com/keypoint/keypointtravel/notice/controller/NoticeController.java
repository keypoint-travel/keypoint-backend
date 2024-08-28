package com.keypoint.keypointtravel.notice.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.request.NoticeRequest;
import com.keypoint.keypointtravel.notice.dto.useCase.NoticeUseCase;
import com.keypoint.keypointtravel.notice.service.NoticeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
        @RequestPart(value = "detail") @Valid NoticeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        NoticeUseCase useCase = new NoticeUseCase(
            thumbnailImage, detailImages, findLanguageValue(request.getLanguage()), request.getTitle(),request.getContent());
        noticeService.saveNotice(useCase);
        return APIResponseEntity.<Void>builder()
            .message("공지등록 성공")
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
