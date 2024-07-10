package com.keypoint.keypointtravel.banner.entity;

public interface BannerCode {

    public String getCode();
    public String getDescription();

    public static <T extends BannerCode> String getDescription(Class<T> enumType, String code) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.getCode().equals(code)) {
                return constant.getDescription();
            }
        }
        return null;
    }

    public static <T extends BannerCode> T getConstant(Class<T> enumType, String description) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.getDescription().equals(description)) {
                return constant;
            }
        }
        return null;
    }
}