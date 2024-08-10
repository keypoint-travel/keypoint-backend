package com.keypoint.keypointtravel.guide.dto.useCase;

import com.keypoint.keypointtravel.guide.dto.request.CreateGuideRequest;
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
}
