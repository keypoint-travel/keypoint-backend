package com.keypoint.keypointtravel.dto.common.response;

import com.keypoint.keypointtravel.common.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.exception.HttpClientException;
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

    public static APIResponseEntity<ErrorDTO> toAPIResponseEntity(GeneralException ex) {
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

    public static ResponseEntity<APIResponseEntity<ErrorDTO>> toResponseEntity(
        GeneralException ex
    ) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(ErrorDTO.toAPIResponseEntity(ex));
    }

    public static ResponseEntity<APIResponseEntity<ErrorDTO>> toResponseEntity(
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
