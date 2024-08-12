package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class ReadGuideInAdminUseCase {

  private Pageable pageable;

  public static ReadGuideInAdminUseCase from(Pageable pageable) {
    return new ReadGuideInAdminUseCase(pageable);
  }
}
