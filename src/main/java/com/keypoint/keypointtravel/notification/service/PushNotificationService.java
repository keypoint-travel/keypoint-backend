package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignInvitePushNotificationEvent.CampaignInviteData;
import com.keypoint.keypointtravel.notification.event.pushNotification.FrendInvitePushNotificationEvent.FrendInviteData;
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
    public PushNotificationDTO generateNotificationDTO(PushNotificationEvent event,
        LanguageCode languageCode) {
        PushNotificationType type = event.getPushNotificationType();
        Object additionalData = event.getAdditionalData();

        switch (type) {
            case RECEIPT_REGISTER -> {

            }
            case FRIEND_INVITE -> {
                if (additionalData instanceof FrendInviteData) {
                    FrendInviteData data = (FrendInviteData) additionalData;

                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format("초대자: %s 캠페인 명: %s", data.getInviteeName(),
                        data.getCampaignName());
                    //MessageSourceUtils.getLocalizedLanguage(type.getContentLangCode(), languageCode.getLocale());

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_START -> {
                if (additionalData instanceof String) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format(" 캠페인 명: %s", additionalData);

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_END -> {
                if (additionalData instanceof String) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format(" 캠페인 명: %s", additionalData);

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_D_DAY -> {
                if (additionalData instanceof String) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format(" 캠페인 명: %s", additionalData);

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_INVITE -> {
                if (additionalData instanceof String) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format(" 캠페인 명: %s", additionalData);

                    return PushNotificationDTO.of(title, content);
                }
            }
            case CAMPAIGN_REGISTRATION -> {
                if (additionalData instanceof CampaignInviteData) {
                    CampaignInviteData data = (CampaignInviteData) additionalData;

                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format("등록한 사람: %s 캠페인 명: %s", data.getRegister(),
                        data.getCampaignName());
                    //MessageSourceUtils.getLocalizedLanguage(type.getContentLangCode(), languageCode.getLocale());

                    return PushNotificationDTO.of(title, content);
                }
            }
            case EVENT_NOTICE -> {
                if (additionalData instanceof String) {
                    // 1. FCM 내용 구성
                    String title = MessageSourceUtils.getLocalizedLanguage(
                        type.getTitleLangCode(), languageCode.getLocale());
                    String content = String.format(" 캠페인 명: %s", additionalData);

                    return PushNotificationDTO.of(title, content);
                }
            }
            case PAYMENT_COMPLETION -> {
                // 1. FCM 내용 구성
                String title = MessageSourceUtils.getLocalizedLanguage(
                    type.getTitleLangCode(), languageCode.getLocale());
                String content =
                    MessageSourceUtils.getLocalizedLanguage(type.getContentLangCode(),
                        languageCode.getLocale());

                return PushNotificationDTO.of(title, content);
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
