package com.keypoint.keypointtravel.badge.dto.useCase;

import com.keypoint.keypointtravel.badge.dto.request.CreateBadgeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateBadgeUseCase {

    private Long badgeId;
    private Integer order;
    private MultipartFile badgeOnImage;
    private MultipartFile badgeOffImage;

    public static UpdateBadgeUseCase of(
        Long badgeId,
        CreateBadgeRequest request,
        MultipartFile badgeOnImage,
        MultipartFile badgeOffImage
    ) {
        return new UpdateBadgeUseCase(
            badgeId,
                request != null ? request.getOrder() : null,
            badgeOnImage,
            badgeOffImage
        );
    }
}
