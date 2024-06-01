package com.keypoint.keypointtravel.dto.common.response;

import com.keypoint.keypointtravel.common.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.exception.HttpClientException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private Object detail;

    public static ResponseEntity<Object> toResponseEntity(GeneralException ex) {
        Object detail = ex.getDetail();
        return ResponseEntity
            .status(ex.getStatus())
            .body(ErrorDTO.builder()
                .code(ex.getErrorCode())
                .msg(ex.getErrorMsg())
                .detail(detail)
                .build());
    }

    public static ResponseEntity<Object> toResponseEntity(
        HttpClientException ex,
        ErrorCode code
    ) {
        return ResponseEntity
            .status(ex.getStatusCode())
            .body(ErrorDTO.builder()
                .code(code.getCode())
                .msg(ex.getMessage())
                .build());
    }

}
