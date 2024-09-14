package com.keypoint.keypointtravel.notice.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.NoticeErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.CreateNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentsUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.PlusNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase;
import com.keypoint.keypointtravel.notice.entity.Notice;
import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import com.keypoint.keypointtravel.notice.repository.NoticeContentRepository;
import com.keypoint.keypointtravel.notice.repository.NoticeRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UploadFileService uploadFileService;
    private final NoticeRepository noticeRepository;
    private final NoticeContentRepository noticeContentRepository;

    @Transactional
    public void saveNotice(CreateNoticeUseCase useCase) {
        try {
            // 1. 썸네일 이미지 저장
            Long thumbnailImageId = saveThumbnailImage(useCase.getThumbnailImage());

            // 2. 공지 생성
            Notice notice = Notice.from(thumbnailImageId);

            // 공지 저장
            noticeRepository.save(notice);

            // 3. 공지 내용 생성 및 저장
            NoticeContent noticeContent = createNoticeContent(
                notice,
                useCase.getTitle(),
                useCase.getContent(),
                LanguageCode.EN
            );

            // 5. NoticeContent 저장
            noticeContentRepository.save(noticeContent);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    @Transactional
    public void saveNoticeByOtherLanguage(PlusNoticeUseCase useCase) {
        // 1. 요청한 언어 버전으로 존재하는지 확인
        if (noticeRepository.isExistNoticeContentByLanguageCode(useCase.getNoticeId(),
            useCase.getLanguageCode())) {
            throw new GeneralException(NoticeErrorCode.EXISTS_NOTICE_CONTENT);
        }

        try {
            // 2. 공지 조회
            Notice notice = noticeRepository.getReferenceById(useCase.getNoticeId());

            // 3. 공지 내용 생성 및 저장
            NoticeContent noticeContent = createNoticeContent(
                notice,
                useCase.getTitle(),
                useCase.getContent(),
                useCase.getLanguageCode()
            );

            // 4. NoticeContent 저장
            noticeContentRepository.save(noticeContent);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    // 썸네일 이미지 저장 로직
    private Long saveThumbnailImage(MultipartFile thumbnailImage) throws IOException {
        if (thumbnailImage != null) {
            return uploadFileService.saveUploadFile(
                thumbnailImage,
                DirectoryConstants.NOTICE_THUMBNAIL_DIRECTORY
            );
        }
        return null;
    }

    /**
     * 공지 내용을 생성하는 메서드
     *
     * @param notice
     * @param title
     * @param content
     * @param language
     * @return
     */
    private NoticeContent createNoticeContent(
        Notice notice,
        String title,
        String content,
        LanguageCode language
    ) {
        return NoticeContent.builder()
            .notice(notice)
            .title(title)
            .content(content)
            .languageCode(language)
            .build();
    }

    public Page<NoticeResponse> findNotices(PageUseCase useCase) {
        try {
            return noticeRepository.findNotices(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 공지 수정 함수
     *
     * @param useCase
     */
    @Transactional
    public void updateNotice(UpdateNoticeUseCase useCase) {
        try {
            Long noticeId = useCase.getNoticeId();

            // 1. 공지 사항 존재 확인
            Optional<Long> longOptional = noticeRepository.findThumbnailImageIdById(noticeId);
            if (longOptional.isEmpty()) {
                throw new GeneralException(NoticeErrorCode.NOT_EXISTED_NOTICE);
            }

            // 1. 썸네일 이미지 업데이트
            if (useCase.getThumbnailImage() != null) {
                uploadFileService.updateUploadFile(
                    longOptional.get(),
                    useCase.getThumbnailImage(),
                    DirectoryConstants.NOTICE_THUMBNAIL_DIRECTORY
                );
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 공지 수정 함수
     *
     * @param useCase
     */
    @Transactional
    public void updateNoticeContent(UpdateNoticeContentUseCase useCase) {
        try {
            Long noticeId = useCase.getNoticeId();
            Long noticeContentId = useCase.getNoticeContentId();

            // 1. 유효성 검사
            if (noticeContentRepository.existsByIdNotAndLanguageCodeAndIsDeletedFalse(
                // 이미 존재하는 언어 코드인지 확인
                noticeId,
                noticeContentId,
                useCase.getLanguageCode()
            )) {
                throw new GeneralException(GuideErrorCode.DUPLICATED_GUIDE_TRANSLATION_LANGUAGE);
            }
            if (noticeContentRepository.existsByIdAndLanguageCodeAndIsDeletedFalse(
                // 영어버전 변경을 시도하는 건지 확인
                noticeContentId,
                LanguageCode.EN
            ) && useCase.getLanguageCode() != LanguageCode.EN) {
                throw new GeneralException(CommonErrorCode.FAIL_TO_DELETE_EN_DATA);
            }

            // 변경사항 저장
            noticeContentRepository.updateNoticeContent(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    private NoticeContent findNoticeByNoticeId(Long noticeId) {
        return noticeContentRepository.findByIdAndIsDeletedFalse(noticeId).orElseThrow(
            () -> new GeneralException(NoticeErrorCode.NOT_EXISTED_NOTICE)
        );
    }

    public NoticeDetailResponse findNoticeById(Long noticeContentId) {
        try {
            return noticeRepository.findNoticeById(noticeContentId);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 공지 삭제 성공
     *
     * @param useCase
     */
    public void deleteNotices(DeleteNoticeUseCase useCase) {
        try {
            noticeRepository.deleteNotices(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public void deleteNoticeContents(DeleteNoticeContentsUseCase useCase) {
        try {
            noticeRepository.deleteNoticeContents(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    @Transactional
    public void deleteNoticeContent(DeleteNoticeContentUseCase useCase) {
        try {
            // 1. 유효성 검사
            if (noticeContentRepository.existsByIdAndLanguageCodeAndIsDeletedFalse(
                useCase.getNoticeContentId(),
                LanguageCode.EN
            )) {
                throw new GeneralException(CommonErrorCode.FAIL_TO_DELETE_EN_DATA);
            }

            // 2. 삭제
            Long result = noticeRepository.deleteNoticeContent(useCase);

            if (result < 0) {
                throw new GeneralException(NoticeErrorCode.NOT_EXISTED_NOTICE);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}

