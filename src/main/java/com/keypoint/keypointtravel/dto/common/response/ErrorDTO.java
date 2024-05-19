package com.keypoint.keypointtravel.dto.common.response;

import com.keypoint.keypointtravel.common.handler.GeneralException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private Object detail;

    public static ResponseEntity<ErrorDTO> toResponseEntity(GeneralException ex) {
        Object detail = ex.getDetail();
        return ResponseEntity
            .status(ex.getStatus())
            .body(ErrorDTO.builder()
                .code(ex.getErrorCode())
                .msg(ex.getErrorMsg())
                .detail(detail)
                .build());
    }

}
