package com.keypoint.keypointtravel.global.enumType.receipt;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptCategory {
    TRANSPORTATION(1, "transportation_expenses"),
    ACCOMMODATION(2, "accommodation_expenses"),
    SHOPPING(3, "shopping_expenses"),
    FOOD(4, "food_expenses"),
    OTHER(5, "other_expenses");

    private final int code;
    private final String description;

    public static ReceiptCategory getConstant(String description) {
        return Arrays.stream(ReceiptCategory.values())
            .filter(constant -> constant.getDescription().equals(description))
            .findFirst()
            .orElseThrow(() -> new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                "ReceiptCategory 변환에 실패하였습니다."));
    }

    public static boolean isExist(String description) {
        return Arrays.stream(ReceiptCategory.values())
            .anyMatch(constant -> constant.getDescription().equals(description) && constant != OTHER);
    }
}

