package com.keypoint.keypointtravel.notice.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.NoticeErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.response.NoticesResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.NoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.PlusNoticeUseCase;
import com.keypoint.keypointtravel.notice.entity.Notice;
import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import com.keypoint.keypointtravel.notice.entity.NoticeDetailImage;
import com.keypoint.keypointtravel.notice.repository.NoticeContentRepository;
import com.keypoint.keypointtravel.notice.repository.NoticeRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UploadFileService uploadFileService;
    private final NoticeRepository noticeRepository;
    private final NoticeContentRepository noticeContentRepository;

    @Transactional
    public void saveNotice(NoticeUseCase useCase) {
        try {
            // 1. 공지 생성
            Notice notice = new Notice();

            // 공지 저장
            noticeRepository.save(notice);

            // 2. 썸네일 이미지 저장
            Long thumbnailImageId = saveThumbnailImage(useCase.getThumbnailImage());

            // 3. 공지 내용 생성 및 저장 (일단 빈 리스트를 넣음, 이미지 추가는 나중에)
            NoticeContent noticeContent = createNoticeContent(
                notice,
                useCase.getTitle(),
                useCase.getContent(),
                useCase.getLanguage(),
                thumbnailImageId,
                new ArrayList<>()
            );

            // 4. 상세 이미지 저장 및 NoticeContent에 연결
            List<NoticeDetailImage> detailImages = saveDetailImages(useCase.getDetailImages(), noticeContent);

            // NoticeContent에 detailImages 설정
            noticeContent.setDetailImages(detailImages);

            // 5. NoticeContent 저장
            noticeContentRepository.save(noticeContent);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    @Transactional
    public void saveNoticeByOtherLanguage(PlusNoticeUseCase useCase) {
        if (noticeRepository.isExistNoticeContentByLanguageCode(useCase.getNoticeId(), useCase.getLanguage())) {
            throw new GeneralException(NoticeErrorCode.EXISTS_NOTICE_CONTENT);
        }

        try {
            // 1. 공지 조회
            Notice notice = noticeRepository.getReferenceById(useCase.getNoticeId());

            // 2. 썸네일 이미지 저장
            Long thumbnailImageId = saveThumbnailImage(useCase.getThumbnailImage());

            // 3. 공지 내용 생성 및 저장
            NoticeContent noticeContent = createNoticeContent(
                notice,
                useCase.getTitle(),
                useCase.getContent(),
                useCase.getLanguage(),
                thumbnailImageId,
                new ArrayList<>()
            );

            // 4. 상세 이미지 저장 및 NoticeContent에 연결
            List<NoticeDetailImage> detailImages = saveDetailImages(useCase.getDetailImages(), noticeContent);

            // NoticeContent에 detailImages 설정
            noticeContent.setDetailImages(detailImages);

            // 5. NoticeContent 저장
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

    // 상세 이미지 저장 로직 (NoticeContent와 연결)
    private List<NoticeDetailImage> saveDetailImages(List<MultipartFile> detailImages, NoticeContent noticeContent) throws IOException {
        List<NoticeDetailImage> noticeDetailImages = new ArrayList<>();
        if (detailImages != null) {
            for (MultipartFile detailImage : detailImages) {
                Long detailImageId = uploadFileService.saveUploadFile(
                    detailImage,
                    DirectoryConstants.NOTICE_DETAIL_DIRECTORY
                );
                // NoticeDetailImage 객체 생성 및 NoticeContent와 연결
                NoticeDetailImage noticeDetailImage = NoticeDetailImage.builder()
                    .noticeContent(noticeContent)  // NoticeContent와 연결
                    .detailImageId(detailImageId)
                    .build();
                noticeDetailImages.add(noticeDetailImage);
            }
        }
        return noticeDetailImages;
    }

    // 공지 내용을 생성하는 메서드 (NoticeDetailImage 리스트는 나중에 설정)
    private NoticeContent createNoticeContent(Notice notice, String title, String content, LanguageCode language, Long thumbnailImageId, List<NoticeDetailImage> detailImages) {
        return NoticeContent.builder()
            .notice(notice)
            .title(title)
            .content(content)
            .languageCode(language)
            .thumbnailImageId(thumbnailImageId)
            .detailImages(detailImages)  // 기본적으로 빈 리스트로 설정
            .build();
    }

    public Page<NoticesResponse> findNotices(PageUseCase useCase) {
        try {
            return noticeRepository.findNotices(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}

