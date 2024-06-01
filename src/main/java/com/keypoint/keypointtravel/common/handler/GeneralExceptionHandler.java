package com.keypoint.keypointtravel.common.handler;

import com.keypoint.keypointtravel.common.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.common.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.exception.HttpClientException;
import com.keypoint.keypointtravel.dto.common.response.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException ex) {
        log.warn(
            String.format("http-status={%s} code={%s} msg={%s} detail={%s}", ex.getStatus().value(),
                ex.getErrorCode(), ex.getErrorMsg(), ex.getDetail())
        );

        return ErrorDTO.toResponseEntity(ex);
    }

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<Object> handleHttpClientException(HttpClientException ex) {
        ErrorCode code = CommonErrorCode.OPEN_API_REQUEST_FAIL;
        log.warn(
            String.format("http-status={%s} code={%s} msg={%s}",
                ex.getStatusCode(),
                code,
                ex.getMessage()
            )
        );

        return ErrorDTO.toResponseEntity(ex, code);
    }
}
