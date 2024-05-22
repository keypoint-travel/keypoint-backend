package com.keypoint.keypointtravel.dto.api.azure.response;

import com.azure.ai.formrecognizer.documentanalysis.models.BoundingRegion;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentSpan;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class OCRDocumentResponse {

    private String docType;
    private List<BoundingRegion> boundingRegions;
    private float confidence;
    private Map<String, OCRDocumentFieldResponse> fields;
    private List<DocumentSpan> spans;
}
