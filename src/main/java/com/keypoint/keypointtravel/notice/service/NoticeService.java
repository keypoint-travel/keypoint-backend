package com.keypoint.keypointtravel.notice.service;

import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.useCase.NoticeUseCase;
import com.keypoint.keypointtravel.notice.entity.Notice;
import com.keypoint.keypointtravel.notice.repository.NoticeRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UploadFileService uploadFileService;

    private final NoticeRepository noticeRepository;

    @Transactional
    public void saveNotice(NoticeUseCase useCase) {
        try {
            // 1. 썸네일 이미지 저장
            Long thumbnailImageId = null;
            if (useCase.getThumbnailImage() != null) {
                thumbnailImageId = uploadFileService.saveUploadFile(
                    useCase.getThumbnailImage(),
                    DirectoryConstants.NOTICE_THUMBNAIL_DIRECTORY
                );
            }

            // 2. 상세 이미지 저장
            List<Long> detailImageIds = new ArrayList<>();
            if (useCase.getDetailImages() != null) {
                for (MultipartFile detailImage : useCase.getDetailImages()) {
                    Long detailImageId = uploadFileService.saveUploadFile(
                        detailImage,
                        DirectoryConstants.NOTICE_DETAIL_DIRECTORY
                    );
                    detailImageIds.add(detailImageId);
                }
            }

            // 3. 공지 저장
            Notice notice = Notice.builder()
                .title(useCase.getTitle())
                .content(useCase.getContent())
                .languageCode(useCase.getLanguage())
                .thumbnailImageId(thumbnailImageId)
                .detailImageIds(detailImageIds)
                .build();

            noticeRepository.save(notice);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }


}
