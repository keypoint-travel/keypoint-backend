package com.keypoint.keypointtravel.dto.api.azure.response;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.azure.ai.formrecognizer.documentanalysis.models.BoundingRegion;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentSpan;
import com.keypoint.keypointtravel.global.enumType.ocr.CurrencyCode;
import com.keypoint.keypointtravel.global.enumType.ocr.OCRFieldName;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class OCRDocumentResponse {

    private String docType;
    private List<BoundingRegion> boundingRegions;
    private float confidence;
    private Map<String, DocumentField> fields;
    private List<DocumentSpan> spans;

    public CurrencyCode getCurrencyCode() {
        DocumentField fieldData = this.fields.get(OCRFieldName.TOTAL.getFieldName());
        String content = fieldData.getContent();

        return CurrencyCode.fromContent(content);
    }
}
