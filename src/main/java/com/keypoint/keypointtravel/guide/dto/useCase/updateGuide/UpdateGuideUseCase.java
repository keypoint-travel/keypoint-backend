package com.keypoint.keypointtravel.guide.dto.useCase.updateGuide;

import com.keypoint.keypointtravel.guide.dto.request.updateGuide.UpdateGuideRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateGuideUseCase {

  private Long guideId;
  private int order;
  private List<UpdateGuideTranslationUseCase> translations;
  private MultipartFile thumbnailImage;

  public static UpdateGuideUseCase of(
      Long guideId,
      UpdateGuideRequest request,
      MultipartFile thumbnailImage
  ) {
    return new UpdateGuideUseCase(
        guideId,
        request.getOrder(),
        request.getTranslations()
            .stream().map(UpdateGuideTranslationUseCase::from)
            .toList(),
        thumbnailImage
    );
  }

}
