package com.keypoint.keypointtravel.external.azure.service;

import com.keypoint.keypointtravel.external.azure.dto.response.OCRResultResponse;
import com.keypoint.keypointtravel.external.azure.dto.useCase.OCRAnalysisUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AzureAPIServiceFallback implements AzureAPIService {

    @Override
    public ResponseEntity<String> requestOCRAnalysis(
        String key,
        OCRAnalysisUseCase request
    ) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public ResponseEntity<OCRResultResponse> requestOCRResult(
        String ocrResultUri,
        String apiVersion,
        String key
    ) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }


}
