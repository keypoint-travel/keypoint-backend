package com.keypoint.keypointtravel.external.azure.service;

import com.keypoint.keypointtravel.external.azure.dto.response.OCRResultResponse;
import com.keypoint.keypointtravel.external.azure.dto.useCase.OCRAnalysisUseCase;
import com.keypoint.keypointtravel.global.constants.AzureAPIConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "azureApiClient", url = AzureAPIConstants.ENDPOINT, fallback = AzureAPIServiceFallback.class)
public interface AzureAPIService {

    @PostMapping(value = AzureAPIConstants.REQUEST_ANALYSIS, headers = "Content-Type=application/json")
    ResponseEntity<String> requestOCRAnalysis(
        @RequestHeader(AzureAPIConstants.AZURE_KEY_HEADER) String key,
        @RequestBody OCRAnalysisUseCase request
    );

    @GetMapping(value = "/{ocrResultPath}", headers = "Content-Type=application/json")
    ResponseEntity<OCRResultResponse> requestOCRResult(
        @PathVariable("ocrResultPath") String ocrResultUri,
        @RequestParam("api-version") String apiVersion,
        @RequestHeader(AzureAPIConstants.AZURE_KEY_HEADER) String key
    );
}
