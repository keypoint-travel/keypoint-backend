package com.keypoint.keypointtravel.common.handler;

import com.keypoint.keypointtravel.common.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.common.enumType.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMsg;
    private final Object detail;

    public GeneralException(HttpStatus status, ErrorCode errorType) {
        this.status = status;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = "";
    }

    public GeneralException(HttpStatus status, ErrorCode errorType, Object detail) {
        this.status = status;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = detail;
    }

    public GeneralException(ErrorCode errorType) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = "";
    }

    public GeneralException(ErrorCode errorType, Object detail) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = detail;
    }


    public GeneralException(ErrorCode errorType, Throwable cause) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = cause.getMessage();
    }

    public GeneralException(Exception exception) {
        if (exception.getClass() == GeneralException.class) {
            GeneralException customException = (GeneralException) exception;
            this.status = customException.getStatus();
            this.errorCode = customException.errorCode;
            this.errorMsg = customException.errorMsg;
            this.detail = customException.getDetail();
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.errorCode = CommonErrorCode.UNKNOWN.getCode();
            this.errorMsg = "";
            this.detail = exception.getMessage();
        }
    }

}
