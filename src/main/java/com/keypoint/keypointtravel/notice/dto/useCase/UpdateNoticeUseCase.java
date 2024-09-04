package com.keypoint.keypointtravel.notice.dto.useCase;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@AllArgsConstructor
public class UpdateNoticeUseCase {
    private Long noticeContentId;
    private MultipartFile thumbnailImage;
    private List<MultipartFile> detailImages;
    private String title;
    private String content;

    public static com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase of(
        Long noticeContentId,
        MultipartFile thumbnailImage,
        List<MultipartFile> detailImages,
        String title,
        String content
    ) {
        return new com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase (
            noticeContentId,
            thumbnailImage,
            detailImages,
            title,
            content
        );
    }

}
