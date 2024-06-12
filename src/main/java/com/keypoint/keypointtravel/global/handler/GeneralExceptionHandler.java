package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import com.keypoint.keypointtravel.dto.common.response.APIResponseEntity;
import com.keypoint.keypointtravel.dto.common.response.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<APIResponseEntity<ErrorDTO>> handleGeneralException(
        GeneralException exception) {
        log.warn(
            String.format("http-status={%s} code={%s} msg={%s} detail={%s}",
                exception.getStatus().value(),
                exception.getErrorCode(),
                exception.getErrorMsg(),
                exception.getDetail()
            )
        );

        return ErrorDTO.toResponseEntity(exception);
    }

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<APIResponseEntity<ErrorDTO>> handleHttpClientException(
        HttpClientException exception
    ) {
        ErrorCode code = CommonErrorCode.OPEN_API_REQUEST_FAIL;
        log.warn(
            String.format("http-status={%s} code={%s} msg={%s}",
                exception.getStatusCode(),
                code,
                exception.getMessage()
            )
        );

        return ErrorDTO.toResponseEntity(exception, code);
    }
}
