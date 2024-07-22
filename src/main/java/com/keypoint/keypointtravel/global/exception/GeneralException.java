package com.keypoint.keypointtravel.global.exception;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final Object detail;

    public GeneralException(HttpStatus status, ErrorCode errorCode) {
        this.status = status;
        this.errorCode = errorCode;
        this.detail = "";
    }

    public GeneralException(HttpStatus status, ErrorCode errorCode, Object detail) {
        this.status = status;
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public GeneralException(ErrorCode errorCode) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
        this.detail = "";
    }

    public GeneralException(ErrorCode errorCode, Object detail) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
        this.detail = detail;
    }


    public GeneralException(ErrorCode errorCode, Throwable cause) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
        this.detail = cause.getMessage();
    }

    public GeneralException(Exception exception) {
        if (exception.getClass() == GeneralException.class) {
            GeneralException customException = (GeneralException) exception;
            this.status = customException.getStatus();
            this.errorCode = customException.errorCode;
            this.detail = customException.getDetail();
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.errorCode = CommonErrorCode.UNKNOWN;
            this.detail = exception.getMessage();
        }
    }

}
