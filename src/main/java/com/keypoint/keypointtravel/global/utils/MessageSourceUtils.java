package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtils {

    private static MessageSource messageSource;

    @Autowired
    public MessageSourceUtils(MessageSource messageSource) {
        MessageSourceUtils.messageSource = messageSource;
    }

    /**
     * messages 에 등록된 다국어를 가져오는 함수
     *
     * @param langCode 다국어 코드
     * @param locale   Locale 객체
     * @return 다국어 메시지
     */
    public static String getLocalizedLanguage(String langCode, Locale locale) {
        try {
            return messageSource.getMessage(langCode, null, locale);
        } catch (NoSuchMessageException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_FIND_LANGUAGE, e);
        }
    }
}
