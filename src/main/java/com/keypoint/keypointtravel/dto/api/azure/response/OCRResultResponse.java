package com.keypoint.keypointtravel.dto.api.azure.response;

import com.keypoint.keypointtravel.common.enumType.ocr.OCROperationStatus;
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
