package com.keypoint.keypointtravel.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignAcceptorPushNotificationEvent.CampaignAcceptorData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignApplicantPushNotificationEvent.CampaignApplicantData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignLeaderPushNotificationEvent.CampaignLeaderData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent.CampaignData;
import com.keypoint.keypointtravel.notification.event.pushNotification.FriendPushNotificationEvent.FriendData;
import com.keypoint.keypointtravel.notification.event.pushNotification.NoticePushNotificationEvent.NoticeData;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.fcmToken.FCMTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final FCMTokenRepository fcmTokenRepository;
    private final CampaignRepository campaignRepository;

    /**
     * Notification 생성
     *
     * @param event        푸시 알림 정보가 존재하는 이벤트
     * @return
     */
    public PushNotificationDTO generateNotificationDTO(
        MemberDetail memberDetail,
        PushNotificationEvent event,
        PushNotificationContent notificationMsg
    ) {
        Locale locale = memberDetail.getLanguage().getLocale();
        PushNotificationType type = event.getPushNotificationType();
        Object additionalData = event.getAdditionalData();

        String title = MessageSourceUtils.getLocalizedLanguage(
            notificationMsg.getTitleLangCode(), locale
        );
        String content = null;
        // 1. FCM 내용 구성
        switch (type) {
            case RECEIPT_REGISTER -> {
                content = notificationMsg.getTranslatedContent(
                    memberDetail.getName(),
                    null,
                    locale
                );
            }
            case CAMPAIGN_D_MINUS_7,
                 CAMPAIGN_D_DAY,
                 CAMPAIGN_NO_EXPENSE_D1,
                 PAYMENT_COMPLETION,
                 INQUIRY_RESPONSE_COMPLETED,
                 CAMPAIGN_D60_PASSED -> {
                content = notificationMsg.getTranslatedContent(
                    null,
                    null,
                    locale
                );
            }
            case CAMPAIGN_INVITE, CAMPAIGN_ACCEPT_INVITEE -> {
                if (additionalData instanceof CampaignLeaderData) {
                    CampaignLeaderData data = (CampaignLeaderData) additionalData;
                    String campaignTitle = campaignRepository.findTitleByCampaignId(
                            data.getCampaignId())
                        .orElse("");

                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        data.getLeaderName(),
                        campaignTitle,
                        locale
                    );
                }
            }
            case CAMPAIGN_ACCEPT_INVITER -> {
                if (additionalData instanceof CampaignAcceptorData) {
                    CampaignAcceptorData data = (CampaignAcceptorData) additionalData;
                    String campaignTitle = campaignRepository.findTitleByCampaignId(
                            data.getCampaignId())
                        .orElse("");

                    // 1. FCM 내용 구성
                    title = notificationMsg.getTranslatedTitle(
                        data.getAcceptorName(),
                        null,
                        locale
                    );
                    content = notificationMsg.getTranslatedContent(
                        null,
                        campaignTitle,
                        locale
                    );
                }
            }
            case CAMPAIGN_END, CAMPAIGN_START -> {
                if (additionalData instanceof CampaignData) {
                    CampaignData data = (CampaignData) additionalData;
                    String campaignTitle = campaignRepository.findTitleByCampaignId(
                            data.getCampaignId())
                        .orElse("");

                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        null,
                        campaignTitle,
                        locale
                    );
                }
            }
            case EVENT_NOTICE -> {
                if (additionalData instanceof NoticeData) {
                    NoticeData data = (NoticeData) additionalData;
                    // TODO 공지 사항 개발이 완료된 후 연결 필요
                    // String noticeTitle = campaignRepository.findTitleByCampaignId(
                    //         data.getNoticeId())
                    //     .orElse("");

                    // // 1. FCM 내용 구성
                    // content = notificationMsg.getTranslatedContent(
                    //     null,
                    //     campaignTitle,
                    //     locale
                    // );
                }
            }
            case CAMPAIGN_JOIN_REQUEST -> {
                if (additionalData instanceof CampaignApplicantData) {
                    CampaignApplicantData data = (CampaignApplicantData) additionalData;
                    String campaignTitle = campaignRepository.findTitleByCampaignId(
                            data.getCampaignId())
                        .orElse("");

                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        data.getApplicantName(),
                        campaignTitle,
                        locale
                    );
                }
            }
            case FRIEND_ADDED, FRIEND_ACCEPTED_RECEIVER -> {
                if (additionalData instanceof FriendData) {
                    FriendData data = (FriendData) additionalData;

                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        data.getFriendName(),
                        null,
                        locale
                    );
                }
            }
            case FRIEND_ACCEPTED_SENDER -> {
                if (additionalData instanceof FriendData) {
                    FriendData data = (FriendData) additionalData;

                    // 1. FCM 내용 구성
                    title = notificationMsg.getTranslatedTitle(
                        data.getFriendName(),
                        null,
                        locale
                    );
                    content = MessageSourceUtils.getLocalizedLanguage(
                        notificationMsg.getContentLangCode(), locale);
                }
            }
            // TODO 문구 미정 항목
//            case CAMPAIGN_START -> {
//                if (additionalData instanceof String) {
//                    // 1. FCM 내용 구성
//                    String title = MessageSourceUtils.getLocalizedLanguage(
//                        type.getTitleLangCode(), languageCode.getLocale());
//                    String content = String.format(" 캠페인 명: %s", additionalData);
//
//                    return PushNotificationDTO.of(title, content);
//                }
//            }
//            case EVENT_NOTICE -> {
//                if (additionalData instanceof String) {
//                    // 1. FCM 내용 구성
//                    String title = MessageSourceUtils.getLocalizedLanguage(
//                        type.getTitleLangCode(), languageCode.getLocale());
//                    String content = String.format(" 캠페인 명: %s", additionalData);
//
//                    return PushNotificationDTO.of(title, content);
//                }
//            }
        }

        return PushNotificationDTO.of(
            notificationMsg,
            title,
            content
        );
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
