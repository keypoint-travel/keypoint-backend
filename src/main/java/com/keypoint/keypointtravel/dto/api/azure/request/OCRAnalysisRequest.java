package com.keypoint.keypointtravel.dto.api.azure.request;

import lombok.Data;

@Data
public class OCRAnalysisRequest {

    private String base64Source; //base64Source

    public OCRAnalysisRequest(String urlSource) {
        this.base64Source = urlSource;
    }

    public static OCRAnalysisRequest toRequest(String urlSource) {
        return new OCRAnalysisRequest(urlSource);
    }
}
