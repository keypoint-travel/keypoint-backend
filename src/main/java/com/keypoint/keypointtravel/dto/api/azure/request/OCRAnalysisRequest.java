package com.keypoint.keypointtravel.dto.api.azure.request;

import lombok.Data;

@Data
public class OCRAnalysisRequest {

    private String urlSource;

    public OCRAnalysisRequest(String urlSource) {
        this.urlSource = urlSource;
    }

    public static OCRAnalysisRequest toRequest(String urlSource) {
        return new OCRAnalysisRequest(urlSource);
    }
}
