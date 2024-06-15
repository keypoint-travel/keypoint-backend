package com.keypoint.keypointtravel.external.azure.dto.response;

import com.keypoint.keypointtravel.global.enumType.ocr.OCROperationStatus;
import lombok.Getter;

@Getter
public class OCRResultResponse {

    private String status;
    private String createdDateTime;
    private String lastUpdatedDateTime;
    private OCRAnalyzeResultResponse analyzeResult;

    public OCROperationStatus getStatus() {
        return OCROperationStatus.fromValue(this.status);
    }
}
