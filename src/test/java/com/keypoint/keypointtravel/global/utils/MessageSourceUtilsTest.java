package com.keypoint.keypointtravel.global.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest
@DisplayName("[유틸리티 테스트] - MessageSourceUtilsTest")
class MessageSourceUtilsTest {

    @Autowired
    private MessageSourceUtils messageSourceUtils;

    @Autowired
    private MessageSource messageSource;

    @DisplayName("모든 다국어에 대해서 깨짐 없이 가져오는지 확인한다.")
    @Test
    void testGetLocalizedLanguage() {
        String resultEN = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.ENGLISH);
        String resultKO = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.KOREAN);
        String resultJA = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.JAPANESE);

        assertEquals(resultEN, "Friend Invite Notification");
        assertEquals(resultKO, "친구 초대 알림");
        assertEquals(resultJA, "友達招待のお知らせ");
    }
}