package com.keypoint.keypointtravel.global.enumType.banner;


import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;

public interface BannerCode {

    public String getCode();

    public String getDescription();

    public static <T extends BannerCode> String getDescription(Class<T> enumType, String code) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.getCode().equals(code)) {
                return constant.getDescription();
            }
        }
        throw new GeneralException(BannerErrorCode.REQUEST_DATA_MISMATCH);
    }

    public static <T extends BannerCode> T getConstant(Class<T> enumType, String description) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.getDescription().equals(description)) {
                return constant;
            }
        }
        throw new GeneralException(BannerErrorCode.REQUEST_DATA_MISMATCH);
    }
}