package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

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
            if (langCode == null || langCode.isBlank()) {
                return "";
            }
            return messageSource.getMessage(langCode, null, locale);
        } catch (NoSuchMessageException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_FIND_LANGUAGE, e);
        }
    }

    /**
     * messages 에 등록에 변수를 지정해서 다국어를 가져오는 함수
     *
     * @param langCode 다국어 코드
     * @param locale   Locale 객체
     * @return 다국어 메시지
     * @oaram variables 다국어 적용할 변수 데이터
     */
    public static String getLocalizedLanguageWithVariables(
        String langCode,
        Object[] variables,
        Locale locale
    ) {
        try {
            return messageSource.getMessage(langCode, variables, locale);
        } catch (NoSuchMessageException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_FIND_LANGUAGE, e);
        }
    }

    public static String getBadgeName(BadgeType badgeType) {
        try {
            Locale currentLocale = LocaleContextHolder.getLocale();
            String languageKey = badgeType.getLanguageKey();
            return MessageSourceUtils.getLocalizedLanguage(
                    languageKey,
                    currentLocale
            );
        } catch (Exception ex) {
            LogUtils.writeErrorLog("BadgeResponse", "Fail to get language " + ex.getMessage());
            return badgeType.getDescription();
        }
    }
}
