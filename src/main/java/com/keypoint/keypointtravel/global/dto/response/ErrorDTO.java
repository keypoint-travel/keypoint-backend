package com.keypoint.keypointtravel.global.dto.response;

import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private Object detail;

    public static APIResponseEntity<ErrorDTO> from(GeneralException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
            .code(ex.getErrorCode())
            .msg(ex.getErrorMsg())
            .detail(ex.getDetail())
            .build();

        return APIResponseEntity.<ErrorDTO>builder()
            .result(false)
            .isAuthError(ex.getStatus() == HttpStatus.UNAUTHORIZED)
            .code(ex.getStatus())
            .data(errorDTO)
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
                .result(false)
                .code(HttpStatus.BAD_REQUEST)
                .data(errorDTO)
                .build());
    }
}
