package com.keypoint.keypointtravel.badge.dto.useCase;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateBadgeUseCase {

    private BadgeType type;
    private MultipartFile badgeOnImage;
    private MultipartFile badgeOffImage;

    public Badge toEntity(
        Long badgeOnImageId,
        Long badgeOffImageId
    ) {
        return new Badge(
            badgeOnImageId,
            badgeOffImageId,
            type.getDescription(),
            type.getDefaultOrder(),
            this.type
        );
    }
}
