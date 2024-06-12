package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.dto.common.response.APIResponseEntity;
import com.keypoint.keypointtravel.dto.common.response.ErrorDTO;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class GeneralResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        String errorMsg = null;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            String field = fieldError.getField();
            String errormessage = fieldError.getDefaultMessage();

            errorMsg = String.format("field: %s, message: %s", field, errormessage);
        }

        GeneralException exception = new GeneralException(
            CommonErrorCode.INVALID_REQUEST_DATA, errorMsg);
        APIResponseEntity<ErrorDTO> apiResponse = ErrorDTO.toAPIResponseEntity(exception);

        return new ResponseEntity<>(apiResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        TypeMismatchException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        String errorMsg = String.format("property: %s, message: %s",
            ex.getPropertyName(),
            ex.getMessage()
        );
        GeneralException exception = new GeneralException(
            CommonErrorCode.INVALID_REQUEST_DATA,
            errorMsg
        );
        APIResponseEntity<ErrorDTO> apiResponse = ErrorDTO.toAPIResponseEntity(exception);

        return new ResponseEntity<>(apiResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        GeneralException exception = new GeneralException(
            CommonErrorCode.INVALID_REQUEST_DATA,
            ex.getMessage()
        );
        APIResponseEntity<ErrorDTO> apiResponse = ErrorDTO.toAPIResponseEntity(exception);

        return new ResponseEntity<>(apiResponse, headers, status);
    }
}
