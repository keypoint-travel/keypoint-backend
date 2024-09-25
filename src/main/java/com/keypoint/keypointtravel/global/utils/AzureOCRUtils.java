package com.keypoint.keypointtravel.global.utils;

import com.azure.ai.formrecognizer.documentanalysis.implementation.models.CurrencyValue;
import com.azure.ai.formrecognizer.documentanalysis.implementation.models.DocumentField;
import com.keypoint.keypointtravel.global.enumType.ocr.OCRFieldName;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class AzureOCRUtils {

    public static String getDocumentValue(
        OCRFieldName fieldName,
        Map<String, DocumentField> fieldMap
    ) {
        try {
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
        } catch (Exception e) {
            LogUtils.writeErrorLog("getDocumentValue",
                String.format("Error occurred while getting document: %s", fieldName));
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
        return fieldData.getValueDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
    }

    private static String getTimeValue(DocumentField fieldData) {
        return fieldData.getValueTime();
    }

    private static String getCurrencyValue(DocumentField fieldData) {
        if (fieldData.getValueArray() == null || fieldData.getValueArray().isEmpty()) {
            return null;
        }

        DocumentField taxDetailsField = fieldData.getValueArray().get(0);
        if (taxDetailsField == null || taxDetailsField.getValueObject() == null) {
            return null;
        }

        DocumentField accountField = taxDetailsField.getValueObject()
            .get(OCRFieldName.AMOUNT.getFieldName());
        if (accountField == null) {
            return null;
        }

        CurrencyValue currencyValue = accountField.getValueCurrency();
        return currencyValue == null ? null : currencyValue.getCurrencyCode();
    }

    public static String extractCardNumber(String data) {
        Pattern pattern = Pattern.compile("\\[\\s*카드번호\\s*\\]\\s*(\\d{4}\\s*\\*+\\s*\\d{4})");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", "");
        }
        return null;
    }

    public static String extractApprovalNumber(String data) {
        Pattern pattern = Pattern.compile("\\[\\s*승인번호\\s*\\] (\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String extractRewardPoints(String data) {
        Pattern pattern = Pattern.compile("\\[\\s*적립포인트\\s*\\] (\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String extractAvailablePoints(String data) {
        Pattern pattern = Pattern.compile("\\[\\s*가용포인트\\s*\\] (\\d+,\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
