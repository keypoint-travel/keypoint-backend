package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignInvitePushNotificationEvent.CampaignInviteData;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.fcmToken.FCMTokenRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final FCMTokenRepository fcmTokenRepository;
    private final PushNotificationHistoryService pushNotificationHistoryService;

    /**
     * Notification 생성
     *
     * @param event        푸시 알림 정보가 존재하는 이벤트
     * @param languageCode 언어 코드
     * @return
     */
    public PushNotificationDTO generateNotification(PushNotificationEvent event,
        LanguageCode languageCode) {
        PushNotificationType type = event.getPushNotificationType();
        Object additionalData = event.getAdditionalData();

        switch (type) {
            case FRIEND_INVITE -> {
                if (additionalData instanceof CampaignInviteData) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = MessageSourceUtils.getLocalizedLanguage(
                        type.getContentLangCode(), languageCode.getLocale());

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_START -> {

            }
            case CAMPAIGN_END -> {

            }
            case CAMPAIGN_D_DAY -> {

            }
            case CAMPAIGN_INVITE -> {

            }
            case CAMPAIGN_REGISTRATION -> {

            }
            case EVENT_NOTICE -> {

            }
            case PAYMENT_COMPLETION -> {

            }
        }

        return null;
    }

    /**
     * FCM 전송에 실패한 토큰 삭제
     *
     * @param failedHashcodes 실패한 Message 데이터
     * @param tokenMapper     Message hashcode가 key로 token 정보가 저장되어 있는 데이터
     */
    public void deleteFailedToken(List<Integer> failedHashcodes, Map<Integer, String> tokenMapper) {
        if (!failedHashcodes.isEmpty()) {
            List<String> failedTokens = new ArrayList<>();
            for (Integer failedHashcode : failedHashcodes) {
                failedTokens.add(tokenMapper.get(failedHashcode));
            }

            fcmTokenRepository.deleteFCMTokenByTokens(failedTokens);
        }
    }
}
