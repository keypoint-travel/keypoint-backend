package com.keypoint.keypointtravel.dto.api.azure.response;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.AddressValue;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.BoundingRegion;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.CurrencyValue;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentSignatureType;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentSpan;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.SelectionMarkState;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class OCRDocumentFieldResponse {

    private List<BoundingRegion> boundingRegions;
    private Float confidence;
    private String content;
    private List<DocumentSpan> spans;
    private String type;
    private AddressValue valueAddress;
    private List<OCRDocumentFieldResponse> valueArray;
    private Boolean valueBoolean;
    private String valueCountryRegion;
    private CurrencyValue valueCurrency;
    private String valueDate;
    private Integer valueInteger;
    private Float valueNumber;
    private Map<String, OCRDocumentFieldResponse> valueObject;
    private String valuePhoneNumber;
    private SelectionMarkState valueSelectionMark;
    private DocumentSignatureType valueSignature;
    private String valueString;
    private String valueTime;
}
