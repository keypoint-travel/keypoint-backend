package com.keypoint.keypointtravel.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponseEntity<T> {

    @Builder.Default
    private boolean result = true;

    @Builder.Default
    private boolean isAuthError = false;

    @Builder.Default
    private HttpStatus code = HttpStatus.OK;

    private String message;

    private T data;

    public static <T> APIResponseEntity<T> success(T data) {
        return APIResponseEntity.<T>builder()
            .result(true)
            .data(data)
            .code(HttpStatus.OK)
            .build();
    }

    public static <T> APIResponseEntity<T> success(T data, String message) {
        return APIResponseEntity.<T>builder()
            .result(true)
            .data(data)
            .message(message)
            .code(HttpStatus.OK)
            .build();
    }

    public static APIResponseEntity<Boolean> failure(String message, HttpStatus status) {
        return APIResponseEntity.<Boolean>builder()
            .result(false)
            .message(message)
            .data(false)
            .code(status)
            .build();
    }

    public static APIResponseEntity<Boolean> failure(String message) {
        return APIResponseEntity.<Boolean>builder()
            .result(false)
            .message(message)
            .data(false)
            .code(HttpStatus.BAD_REQUEST)
            .build();
    }
}

