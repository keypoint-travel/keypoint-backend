package com.keypoint.keypointtravel.controller;

import com.keypoint.keypointtravel.dto.recipt.request.ReceiptRequest;
import com.keypoint.keypointtravel.dto.recipt.response.ReceiptDTO;
import com.keypoint.keypointtravel.service.api.AzureOCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
public class DevController {

    private final AzureOCRService azureOCRService;

    @PostMapping("/azure/ocr")
    public ReceiptDTO getReceiptResult(@RequestBody ReceiptRequest request) {
        return azureOCRService.analyzeReceipt(request.getUrl());
    }
}
