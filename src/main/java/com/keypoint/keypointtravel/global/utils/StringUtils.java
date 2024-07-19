package com.keypoint.keypointtravel.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

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

    /**
     * 파일명에서 파일 확장자를 찾는 함수
     *
     * @param filename
     * @return
     */
    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
    
    /**
     * 원하는 숫자 자리 수 길이의 랜덤 숫자를 반환하는 함수
     * @param digits 숫자 자리 수
     * @return 랜덤 번호(String)
     */
    public static String getRandomNumber(int digits) {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        Random random = new Random(Long.parseLong(nowDate));

        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            randomNumber.append(random.nextInt(10));
        }
        
        return randomNumber.toString();
    }

}
