package com.keypoint.keypointtravel.common.utils;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.CurrencyValue;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.keypoint.keypointtravel.common.enumType.ocr.OCRFieldName;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AzureOCRUtils {

    public static String getDocumentValue(
        OCRFieldName fieldName,
        Map<String, DocumentField> fieldMap
    ) {
        DocumentField fieldData = fieldMap.get(fieldName.getFieldName());
        if (fieldData == null) {
            return null;
        }

        switch (fieldName.getType()) {
            case ADDRESS:
                return getAddressValue(fieldData);
            case STRING:
                return getStringValue(fieldData);
            case PHONE_NUMBER:
                return getPhoneNumberValue(fieldData);
            case NUMBER:
                return getNumberValue(fieldData).toString();
            case DATE:
                return getDateValue(fieldData);
            case TIME:
                return getTimeValue(fieldData);
            case CURRENCY:
                return getCurrencyValue(fieldData);
            default:
                return null;
        }
    }

    private static String getAddressValue(DocumentField fieldData) {
        return fieldData.getContent();
    }

    private static String getStringValue(DocumentField fieldData) {
        return fieldData.getContent();
    }

    private static String getPhoneNumberValue(DocumentField fieldData) {
        return fieldData.getContent();
    }

    private static Float getNumberValue(DocumentField fieldData) {
        return fieldData.getValueNumber();
    }

    private static String getDateValue(DocumentField fieldData) {
        return fieldData.getValueDate().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
    }

    private static String getTimeValue(DocumentField fieldData) {
        return fieldData.getValueTime();
    }

    private static String getCurrencyValue(DocumentField fieldData) {
        if (fieldData.getValueArray() == null || fieldData.getValueArray().isEmpty()){
            return null;
        }

        DocumentField taxDetailsField = fieldData.getValueArray().get(0);
        if (taxDetailsField == null || taxDetailsField.getValueObject() == null) {
            return null;
        }

        DocumentField accountField = taxDetailsField.getValueObject().get(OCRFieldName.AMOUNT.getFieldName());
        if (accountField == null) {
            return null;
        }

        CurrencyValue currencyValue = accountField.getValueCurrency();
        return currencyValue == null ? null : currencyValue.getCurrencyCode();
    }
}
