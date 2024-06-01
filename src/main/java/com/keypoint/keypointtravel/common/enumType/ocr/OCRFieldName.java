package com.keypoint.keypointtravel.common.enumType.ocr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OCRFieldName {
    MERCHANT_ADDRESS("MerchantAddress", OCRFieldType.ADDRESS),
    MERCHANT_NAME("MerchantName", OCRFieldType.STRING),
    MERCHANT_PHONE_NUMBER("MerchantPhone", OCRFieldType.PHONE_NUMBER),
    TOTAL("Total", OCRFieldType.NUMBER),
    TRANSACTION_DATE("TransactionDate", OCRFieldType.DATE),
    TRANSACTION_TIME("TransactionTime", OCRFieldType.TIME),
    SUBTOTAL("Subtotal", OCRFieldType.NUMBER),
    TOTAL_TAX("TotalTax", OCRFieldType.NUMBER),
    TIP("Tip", OCRFieldType.NUMBER),
    ARRIVAL_DATE("ArrivalDate", OCRFieldType.DATE), // 호텔 영수증
    DEPARTURE_DATE("DepartureDate", OCRFieldType.DATE), // 호텔 영수증
    CURRENCY("Currency", OCRFieldType.STRING),

    TOTAL_PRICE("TotalPrice", OCRFieldType.NUMBER),
    DESCRIPTION("Description", OCRFieldType.STRING),
    QUANTITY("Quantity", OCRFieldType.NUMBER),
    PRODUCT_CODE("ProductCode", OCRFieldType.STRING),
    QUANTITY_UNIT("QuantityUnit", OCRFieldType.STRING),
    DATE("Date", OCRFieldType.DATE), // 호텔 영수증
    CATEGORY("Category", OCRFieldType.STRING), // 호텔 영수증

    TAX_DETAILS("TaxDetails", OCRFieldType.CURRENCY),

    AMOUNT("Amount", OCRFieldType.CURRENCY),

    ITEMS("Items", OCRFieldType.ARRAY);

    private final String fieldName;
    private final OCRFieldType type;
}
