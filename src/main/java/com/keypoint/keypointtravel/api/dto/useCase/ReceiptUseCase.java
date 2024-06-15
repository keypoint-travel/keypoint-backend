package com.keypoint.keypointtravel.api.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ReceiptUseCase {

  private MultipartFile file;

  public static ReceiptUseCase from(MultipartFile file) {
    return new ReceiptUseCase(file);
  }
}
