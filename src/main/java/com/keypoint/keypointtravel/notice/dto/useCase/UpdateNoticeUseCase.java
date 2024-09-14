package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@AllArgsConstructor
public class UpdateNoticeUseCase {

    private Long noticeId;
    private MultipartFile thumbnailImage;

    public static UpdateNoticeUseCase of(
        Long noticeContentId,
        MultipartFile thumbnailImage
    ) {
        return new UpdateNoticeUseCase(
            noticeContentId,
            thumbnailImage
        );
    }
}
