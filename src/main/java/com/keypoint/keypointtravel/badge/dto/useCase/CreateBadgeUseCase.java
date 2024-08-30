package com.keypoint.keypointtravel.badge.dto.useCase;

import com.keypoint.keypointtravel.badge.dto.request.CreateBadgeRequest;
import com.keypoint.keypointtravel.badge.entity.Badge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateBadgeUseCase {

    private String name;
    private int order;
    private MultipartFile badgeOnImage;
    private MultipartFile badgeOffImage;

    public static CreateBadgeUseCase of(
        CreateBadgeRequest request,
        MultipartFile badgeOnImage,
        MultipartFile badgeOffImage
    ) {
        return new CreateBadgeUseCase(
            "",
            request.getOrder(),
            badgeOnImage,
            badgeOffImage
        );
    }

    public Badge toEntity(
        Long badgeOnImageId,
        Long badgeOffImageId
    ) {
        return new Badge(
            badgeOnImageId,
            badgeOffImageId,
            this.name,
            this.order
        );
    }
}
