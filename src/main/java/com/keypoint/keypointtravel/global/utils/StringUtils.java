package com.keypoint.keypointtravel.global.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    public static Float convertToFloat(String str) {
        return str == null ? null : Float.parseFloat(str);
    }

    public static Integer convertToInteger(String str) {
        return str == null ? null : Integer.parseInt(str);
    }

    public static String parseGrantTypeInToken(String strGrantType, String token) {
        if (token.startsWith(strGrantType + " ")) {
            return token.substring(strGrantType.length() + 1);
        }

        return token;
    }

    public static Boolean checkPasswordValidation(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

}
