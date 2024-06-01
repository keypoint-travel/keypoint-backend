package com.keypoint.keypointtravel.controller;

import com.keypoint.keypointtravel.dto.recipt.response.ReceiptDTO;
import com.keypoint.keypointtravel.service.api.AzureOCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/open-api")
public class OpenAPIController {

  private final AzureOCRService azureOCRService;

  @PostMapping("/ocr")
  public ReceiptDTO getReceiptResult(@RequestPart MultipartFile file) {
    return azureOCRService.analyzeReceipt(file);
  }
}
