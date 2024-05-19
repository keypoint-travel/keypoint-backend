package com.keypoint.keypointtravel.common.converter;


import com.keypoint.keypointtravel.common.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.utils.cryptography.AES256Utils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

@Converter
public class AES256ToStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (!StringUtils.hasText(attribute)) {
            return attribute;
        }
        try {
            return AES256Utils.encryptAES256(attribute);
        } catch (Exception e) {
            throw new GeneralException(CommonErrorCode.CRYPTOGRAPHY_FAILED, "암호화에 실패하였습니다.");
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }
            return AES256Utils.decryptAES256(dbData);
        } catch (Exception e) {
            throw new GeneralException(CommonErrorCode.CRYPTOGRAPHY_FAILED, "복호화에 실패하였습니다.");
        }
    }
}