package com.keypoint.keypointtravel.external.azure.dto.request;

import lombok.Data;

@Data
public class OCRAnalysisRequest {

    private String base64Source;

    public OCRAnalysisRequest(String base64Source) {
        this.base64Source = base64Source;
    }

    public static OCRAnalysisRequest toRequest(String base64Source) {
        return new OCRAnalysisRequest(base64Source);
    }
}
