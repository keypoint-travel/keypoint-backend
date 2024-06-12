package com.keypoint.keypointtravel.global.enumType.ocr;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OCROperationStatus {
    FAILED("failed"),
    NOT_STARTED("notStarted"),
    RUNNING("running"),
    SUCCEEDED("succeeded");

    private static final Map<String, OCROperationStatus> VALUES_MAP = new ConcurrentHashMap<>();

    static {
        for (OCROperationStatus status : values()) {
            VALUES_MAP.put(status.getValue(), status);
        }
    }

    private final String value;

    public static OCROperationStatus fromValue(String value) {
        OCROperationStatus status = VALUES_MAP.get(value);
        if (status != null) {
            return status;
        }

        throw new GeneralException(
            CommonErrorCode.INVALID_REQUEST_DATA,
            "OCROperationStatus 값을 찾을 수 없습니다."
        );
    }
}
