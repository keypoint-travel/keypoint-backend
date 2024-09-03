package com.keypoint.keypointtravel.external.azure.dto.useCase;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.keypoint.keypointtravel.api.dto.response.ReceiptItemUseCase;
import com.keypoint.keypointtravel.external.azure.dto.response.OCRDocumentResponse;
import com.keypoint.keypointtravel.global.enumType.ocr.CurrencyCode;
import com.keypoint.keypointtravel.global.enumType.ocr.OCRFieldName;
import com.keypoint.keypointtravel.global.utils.AzureOCRUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WholeReceiptUseCase {

    private String receiptType; // 영수증 타입
    private String merchantAddress;
    private String merchantName;
    private String merchantPhoneNumber;
    private Float total;
    private CurrencyCode currencyCode; // 돈 단위 (ISO 4217 기준)
    private String transactionDate; // YYYY-MM-DD
    private String transactionTime; // HH:mm:ss
    private Float subtotal; // total에서 tax를 뺀 금액
    private Float tax;
    private Float tip;

    private String cardNumber; // 카드 번호
    private String approvalNumber; // 승인 번호
    private String rewardPoint; // 적립 포인트
    private String availablePoint; // 가용 포인트

    private List<ReceiptItemUseCase> items; // 결제 항목에 대한 세부 데이터

    @Builder
    public WholeReceiptUseCase(
        String receiptType,
        String merchantName,
        String merchantPhoneNumber,
        String merchantAddress,
        String total,
        CurrencyCode currencyCode,
        String transactionDate,
        String transactionTime,
        String subtotal,
        String tax,
        String tip,
        List<ReceiptItemUseCase> items
    ) {
        this.receiptType = receiptType;
        this.merchantName = merchantName;
        this.merchantPhoneNumber = merchantPhoneNumber;
        this.merchantAddress = merchantAddress;
        this.total = StringUtils.convertToFloat(total);
        this.currencyCode = currencyCode;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.subtotal = StringUtils.convertToFloat(subtotal);
        this.tax = StringUtils.convertToFloat(tax);
        this.tip = StringUtils.convertToFloat(tip);
        this.items = items;
    }

    public static WholeReceiptUseCase from(OCRDocumentResponse response) {
        if (response == null) {
            return null;
        }

        Map<String, DocumentField> fieldMap = response.getFields();

        return WholeReceiptUseCase.builder()
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
                ReceiptItemUseCase.toDTOList(
                    fieldMap.get(OCRFieldName.ITEMS.getFieldName())
                        .getValueArray()
                )
            )
            .currencyCode(response.getCurrencyCode())
            .build();
    }

    public TempReceipt toEntity() {
        return TempReceipt.builder()
                .receiptType(receiptType)
                .merchantPhoneNumber(merchantPhoneNumber)
                .currencyCode(currencyCode)
                .subtotal(subtotal)
                .tax(tax)
                .tip(tip)
                .build();
    }
}
