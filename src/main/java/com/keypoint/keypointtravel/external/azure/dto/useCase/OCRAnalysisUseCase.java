package com.keypoint.keypointtravel.external.azure.dto.useCase;

import lombok.Data;

@Data
public class OCRAnalysisUseCase {

    private String base64Source;

    public OCRAnalysisUseCase(String base64Source) {
        this.base64Source = base64Source;
    }

    public static OCRAnalysisUseCase from(String base64Source) {
        return new OCRAnalysisUseCase(base64Source);
    }
}
