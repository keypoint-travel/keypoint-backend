package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.ErrorDTO;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<APIResponseEntity<ErrorDTO>> handleGeneralException(
        GeneralException exception) {

        return ResponseEntity
            .status(exception.getStatus())
            .body(ErrorDTO.from(exception));
    }

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<APIResponseEntity<ErrorDTO>> handleHttpClientException(
        HttpClientException exception
    ) {
        ErrorCode code = CommonErrorCode.OPEN_API_REQUEST_FAIL;

        return ErrorDTO.of(exception, code);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<APIResponseEntity<ErrorDTO>> handleAuthenticationCredentialsNotFoundException(
        AuthenticationCredentialsNotFoundException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(APIResponseEntity.<ErrorDTO>builder()
                .data(ErrorDTO.from(TokenErrorCode.EXPIRED_TOKEN))
                .build()
            );
    }
}
