package com.keypoint.keypointtravel.external.azure.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class OCRAnalyzeResultResponse {

    private String apiVersion;
    private String modelId;
    private String content;
    private List<Object> pages;
    private List<OCRDocumentResponse> documents;
    private String stringIndexType;
}
