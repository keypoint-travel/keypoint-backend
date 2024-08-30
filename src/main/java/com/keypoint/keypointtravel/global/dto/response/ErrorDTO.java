package com.keypoint.keypointtravel.global.dto.response;

import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private Object detail;

    public static APIResponseEntity<ErrorDTO> from(GeneralException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
            .code(ex.getErrorCode().getCode())
            .msg(getTranslatedMsg(ex.getErrorCode()))
            .detail(ex.getDetail())
            .build();

        return APIResponseEntity.<ErrorDTO>builder()
            .data(errorDTO)
            .build();
    }

    public static ErrorDTO from(ErrorCode errorCode) {
        return ErrorDTO.builder()
            .code(errorCode.getCode())
            .msg(getTranslatedMsg(errorCode))
            .build();
    }


    public static ResponseEntity<APIResponseEntity<ErrorDTO>> of(
        HttpClientException ex,
        ErrorCode code
    ) {
        ErrorDTO errorDTO = ErrorDTO.builder()
            .code(code.getCode())
            .msg(getTranslatedMsg(code))
            .build();

        return ResponseEntity
            .status(ex.getStatusCode())
            .body(APIResponseEntity.<ErrorDTO>builder()
                .data(errorDTO)
                .build());
    }

    public static String getTranslatedMsg(ErrorCode errorCode) {
        try {
            Locale currentLocale = LocaleContextHolder.getLocale();
            return MessageSourceUtils.getLocalizedLanguage(
                errorCode.getCode(),
                currentLocale
            );

        } catch (Exception ex) {
            LogUtils.writeErrorLog("getTranslatedMsg",
                "Fail to translated error code " + errorCode.getCode());

            return errorCode.getMsg();
        }
    }
}
