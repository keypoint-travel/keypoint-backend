package com.keypoint.keypointtravel.global.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


@Component
public class StringUtils {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    private static final String RANDOM_ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    private static final String COUNTER_KEY = "unique_id_counter";
    private static final String DATE_KEY = "unique_id_date";

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public StringUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static Float convertToFloat(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        return Float.parseFloat(str);
    }

    public static Integer convertToInteger(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }

        return Integer.parseInt(str);
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
     *
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

    /**
     * 원하는 자리 수 길이의 랜덤 숫자와 문자의 혼합된 문자열을 반환하는 함수
     *
     * @param chars 문자 자리 수
     * @return 랜덤 번호(String)
     */
    public static String getRandomString(int chars) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < chars; i++) {
            stringBuilder.append(
                RANDOM_ALPHANUMERIC.charAt(random.nextInt(RANDOM_ALPHANUMERIC.length())));
        }
        stringBuilder.append((char) (random.nextInt(26) + 'A'));
        return stringBuilder.toString();
    }

    /**
     * 마크다운 문자열에서 텍스트만 반환하는 함수
     *
     * @param markdown
     * @return
     */
    public static String stripMarkdown(String markdown) {
        if (markdown == null) {
            return null;
        }

        // 1. Remove Markdown headers (e.g., # Header, ## Header)
        String text = markdown.replaceAll("(?m)^#+\\s*", "");

        // 2. Remove Markdown emphasis (e.g., *italic*, **bold**)
        text = text.replaceAll("\\*{1,2}|_{1,2}", "");

        // 3. Remove strikethrough (e.g., ~~strikethrough~~)
        text = text.replaceAll("~~", "");

        // 4. Remove links (e.g., [title](http://example.com))
        text = text.replaceAll("\\[([^\\]]+)\\]\\([^\\)]+\\)", "$1");

        // 5. Remove inline code (e.g., `code`)
        text = text.replaceAll("`+", "");

        // 6. Remove images (e.g., ![alt text](image.jpg))
        text = text.replaceAll("!\\[([^\\]]*)\\]\\([^\\)]+\\)", "$1");

        // 7. Remove blockquotes (e.g., > Blockquote)
        text = text.replaceAll("(?m)^>\\s*", "");

        // 8. Remove horizontal rules (e.g., --- or ***)
        text = text.replaceAll("(?m)^[-*]{3,}\\s*$", "");

        // 9. Remove lists (e.g., - item, * item, 1. item)
        text = text.replaceAll("(?m)^\\s*[-*+]\\s+", "");
        text = text.replaceAll("(?m)^\\s*\\d+\\.\\s+", "");

        // 10. Remove tables (e.g., | header | header |, |---|---|)
        text = text.replaceAll("\\|", "");
        text = text.replaceAll("(?m)^\\s*-{3,}\\s*$", "");

        return text.trim();
    }

    /**
     * 고유번호 생성하는 함수
     * - 날짜가 변경될 때마다 카운터 초기화
     *
     * @return 고유번호
     */
    public static String generateUniqueNumber() {
        String currentDate = dateFormat.format(new Date());
        String storedDate = redisTemplate.opsForValue().get(DATE_KEY);

        if (!currentDate.equals(storedDate)) {
            redisTemplate.opsForValue().set(DATE_KEY, currentDate);
            redisTemplate.opsForValue().set(COUNTER_KEY, "0");
        }

        Long counter = redisTemplate.opsForValue().increment(COUNTER_KEY);
        return currentDate + String.format("%05d", counter);
    }
}
