package com.keypoint.keypointtravel.notice.dto.useCase;

import com.keypoint.keypointtravel.notice.dto.request.CreateNoticeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateNoticeUseCase {
    private MultipartFile thumbnailImage;
    private String title;
    private String content;

    public static CreateNoticeUseCase of(
        MultipartFile thumbnailImage,
        CreateNoticeRequest request
    ) {
        return new CreateNoticeUseCase(thumbnailImage, request.getTitle(), request.getContent());
    }


}
