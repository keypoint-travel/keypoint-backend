package com.keypoint.keypointtravel.dto.recipt.response;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.keypoint.keypointtravel.global.enumType.ocr.OCRFieldName;
import com.keypoint.keypointtravel.global.utils.AzureOCRUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class ReceiptItemDTO {

    private Float totalPrice;
    private Float quantity; // 구매 수
    private String description;
    private String productCode;
    private String quantityUnit;


    @Builder
    public ReceiptItemDTO(
        String totalPrice,
        String quantity,
        String description,
        String productCode,
        String quantityUnit
    ) {
        this.totalPrice = StringUtils.convertToFloat(totalPrice);
        this.quantity = StringUtils.convertToFloat(quantity);
        this.description = description;
        this.productCode = productCode;
        this.quantityUnit = quantityUnit;
    }

    public static ReceiptItemDTO toDTO(DocumentField field) {
        Map<String, DocumentField> fieldMap = field.getValueObject();
        return ReceiptItemDTO.builder()
            .totalPrice(
                AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TOTAL_PRICE,
                    fieldMap
                )
            )
            .description(
                AzureOCRUtils.getDocumentValue(
                    OCRFieldName.DESCRIPTION,
                    fieldMap
                )
            )
            .productCode(
                AzureOCRUtils.getDocumentValue(
                    OCRFieldName.PRODUCT_CODE,
                    fieldMap
                )
            )
            .quantityUnit(
                AzureOCRUtils.getDocumentValue(
                    OCRFieldName.QUANTITY_UNIT,
                    fieldMap
                )
            )
            .build();
    }

    public static List<ReceiptItemDTO> toDTOList(List<DocumentField> fieldList) {
        return fieldList.stream()
            .map(field -> ReceiptItemDTO.toDTO(field))
            .toList();
    }
}
