package com.keypoint.keypointtravel.dto.recipt.response;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.keypoint.keypointtravel.common.enumType.ocr.OCRFieldName;
import com.keypoint.keypointtravel.common.utils.AzureOCRUtils;
import com.keypoint.keypointtravel.common.utils.StringUtils;
import com.keypoint.keypointtravel.dto.api.azure.response.OCRDocumentResponse;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class ReceiptDTO {

    private String receiptType; // 영수증 타입
    private String merchantAddress;
    private String merchantName;
    private String merchantPhoneNumber;
    private Float total;
    private String currencyCode; // 돈 단위 (ISO 4217 기준)
    private String transactionDate; // YYYY-MM-DD
    private String transactionTime; // HH:mm:ss
    private Float subtotal; // total에서 tax를 뺀 금액
    private Float tax;
    private Float tip;

    private List<ReceiptItemDTO> items; // 결제 항목에 대한 세부 데이터

    @Builder
    public ReceiptDTO(
        String receiptType,
        String merchantName,
        String merchantPhoneNumber,
        String merchantAddress,
        String total,
        String currencyCode,
        String transactionDate,
        String transactionTime,
        String subtotal,
        String tax,
        String tip,
        List<ReceiptItemDTO> items
    ) {
        this.receiptType = receiptType;
        this.merchantName = merchantName;
        this.merchantPhoneNumber = merchantPhoneNumber;
        this.merchantAddress = merchantAddress;
        this.total = StringUtils.changeStringToFloat(total);
        this.currencyCode = currencyCode;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.subtotal = StringUtils.changeStringToFloat(subtotal);
        this.tax = StringUtils.changeStringToFloat(tax);
        this.tip = StringUtils.changeStringToFloat(tip);
        this.items = items;
    }

    public static ReceiptDTO toDTO(OCRDocumentResponse response) {
        Map<String, DocumentField> fieldMap = response.getFields();

        return ReceiptDTO.builder()
            .receiptType(response.getDocType())
            .merchantName(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.MERCHANT_NAME,
                    fieldMap
                )
            )
            .merchantPhoneNumber(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.MERCHANT_PHONE_NUMBER,
                    fieldMap
                )
            )
            .merchantAddress(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.MERCHANT_ADDRESS,
                    fieldMap
                )
            )
            .total(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TOTAL,
                    fieldMap
                )
            )
            .transactionDate(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TRANSACTION_DATE,
                    fieldMap
                )

            )
            .transactionTime(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TRANSACTION_TIME,
                    fieldMap
                )
            )
            .subtotal(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.SUBTOTAL,
                    fieldMap
                )
            )
            .tax(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TOTAL_TAX,
                    fieldMap
                )
            )
            .tip(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TIP,
                    fieldMap
                )
            )
            .items(fieldMap.get(OCRFieldName.ITEMS.getFieldName()) == null ?
                null :
                ReceiptItemDTO.toDTOList(
                    fieldMap.get(OCRFieldName.ITEMS.getFieldName())
                        .getValueArray()
                )
            )
            .currencyCode(AzureOCRUtils.getDocumentValue(
                    OCRFieldName.TAX_DETAILS,
                    fieldMap
                )
            )
            .build();
    }
}
