package com.keypoint.keypointtravel.api.dto.useCase;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ReceiptUseCase {

  private MultipartFile file;

  public ReceiptUseCase(MultipartFile file) {
    this.file = file;
  }

  public static ReceiptUseCase from(MultipartFile file) {
    return new ReceiptUseCase(file);
  }
}
