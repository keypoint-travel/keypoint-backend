package com.keypoint.keypointtravel.controller;

import com.keypoint.keypointtravel.dto.api.azure.response.OCRResultResponse;
import com.keypoint.keypointtravel.service.api.AzureOCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
public class DevController {

    private final AzureOCRService azureOCRService;

    @GetMapping("/azure/ocr")
    public OCRResultResponse getReceiptResult() {
        return azureOCRService.analyzeReceipt();
    }
}
