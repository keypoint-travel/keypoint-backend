package com.keypoint.keypointtravel.global.dto.response;

import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import lombok.Builder;
import lombok.Data;
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
            .msg(ex.getErrorCode().getMsg())
            .detail(ex.getDetail())
            .build();

        return APIResponseEntity.<ErrorDTO>builder()
            .data(errorDTO)
            .build();
    }

    public static ErrorDTO from(ErrorCode errorCode) {
        return ErrorDTO.builder()
            .code(errorCode.getCode())
            .msg(errorCode.getMsg())
            .build();
    }


    public static ResponseEntity<APIResponseEntity<ErrorDTO>> of(
        HttpClientException ex,
        ErrorCode code
    ) {
        ErrorDTO errorDTO = ErrorDTO.builder()
            .code(code.getCode())
            .msg(ex.getMessage())
            .build();

        return ResponseEntity
            .status(ex.getStatusCode())
            .body(APIResponseEntity.<ErrorDTO>builder()
                .data(errorDTO)
                .build());
    }
}
