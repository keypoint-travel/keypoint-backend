package com.keypoint.keypointtravel.guide.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.request.CreateGuideRequest;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.entity.GuideTranslation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CreateGuideUseCase {

    private String title;
    private String subTitle;
    private String content;
    private int order;
    private MultipartFile thumbnailImage;

    public static CreateGuideUseCase of(
        CreateGuideRequest request,
        MultipartFile thumbnailImage
    ) {
        return new CreateGuideUseCase(
            request.getTitle(),
            request.getSubTitle(),
            request.getContent(),
            request.getOrder(),
            thumbnailImage
        );
    }

    public Guide toGuideEntity(Long thumbnailId) {
        return new Guide(thumbnailId, this.order);
    }

    public GuideTranslation toGuideTranslationEntity(Guide guide) {
        return GuideTranslation.builder()
            .guide(guide)
            .title(this.title)
            .subTitle(this.subTitle)
            .content(this.content)
            .languageCode(LanguageCode.EN)
            .build();
    }
}
