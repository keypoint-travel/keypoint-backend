package com.keypoint.keypointtravel.api.controller;

import com.keypoint.keypointtravel.api.dto.response.ReceiptDTO;
import com.keypoint.keypointtravel.external.azure.service.AzureOCRService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
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
    public APIResponseEntity<ReceiptDTO> getReceiptResult(@RequestPart("file") MultipartFile file) {
        ReceiptDTO result = azureOCRService.analyzeReceipt(file);
        return APIResponseEntity.<ReceiptDTO>builder()
            .data(result)
            .build();
    }
}
