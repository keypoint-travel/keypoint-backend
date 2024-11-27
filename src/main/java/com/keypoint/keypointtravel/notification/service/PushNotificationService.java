package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.badge.service.MemberBadgeService;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.notice.repository.NoticeContentRepository;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.dto.response.fcmBody.FCMBadgeDetailResponse;
import com.keypoint.keypointtravel.notification.event.pushNotification.AdminPushNotificationEvent.FCMContentData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignAcceptorPushNotificationEvent.CampaignAcceptorData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignApplicantPushNotificationEvent.CampaignApplicantData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignLeaderPushNotificationEvent.CampaignLeaderData;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent.CampaignData;
import com.keypoint.keypointtravel.notification.event.pushNotification.FriendPushNotificationEvent.FriendData;
import com.keypoint.keypointtravel.notification.event.pushNotification.NoticePushNotificationEvent.NoticeData;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.fcmToken.FCMTokenRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final FCMTokenRepository fcmTokenRepository;
    private final CampaignRepository campaignRepository;
    private final MemberBadgeService memberBadgeService;
    private final BadgeRepository badgeRepository;
    private final NoticeContentRepository noticeContentRepository;

    /**
     * Notification 생성
     *
     * @param event        푸시 알림 정보가 존재하는 이벤트
     * @return
     */
    public PushNotificationDTO generateNotificationDTO(
        String memberName,
        LanguageCode languageCode,
        PushNotificationEvent event,
        PushNotificationContent notificationMsg
    ) {
        Locale locale = languageCode.getLocale();
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
                    memberName, // TODO 업로드한 사람 이름으로 변경 필요
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
                if (additionalData instanceof CampaignLeaderData data) {
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
                if (additionalData instanceof CampaignAcceptorData data) {
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
                if (additionalData instanceof CampaignData data) {
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
                if (additionalData instanceof NoticeData data) {
                    String noticeTitle = noticeContentRepository.findTitleByNoticeContentId(
                        data.getNoticeContentId());

                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        noticeTitle,
                        null,
                        locale
                    );
                }
            }
            case CAMPAIGN_JOIN_REQUEST -> {
                if (additionalData instanceof CampaignApplicantData data) {
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
                if (additionalData instanceof FriendData data) {
                    // 1. FCM 내용 구성
                    content = notificationMsg.getTranslatedContent(
                        data.getFriendName(),
                        null,
                        locale
                    );
                }
            }
            case FRIEND_ACCEPTED_SENDER -> {
                if (additionalData instanceof FriendData data) {
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
            case PUSH_NOTIFICATION_BY_ADMIN -> {
                if (additionalData instanceof FCMContentData data) {
                    title = data.getTitle();
                    content = data.getContent();
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


    /**
     * FCM 알림을 구성할 Detail 생성
     *
     * @param memberId
     * @param type
     * @return
     */
    @Transactional
    public Object generateNotificationDetail(Long memberId, PushNotificationType type) {
        // 배지 타입 찾기
        BadgeType badgeType = getBadgeTypeForNotification(type);

        // 배지 발급을 하는 알림 타입인 경우, 배지 발급
        if (badgeType != null) {
            return createBadgeDetailResponse(memberId, badgeType);
        }

        return null;
    }

    /**
     * 알림 타입에 따라 배지 타입을 반환
     *
     * @param type 푸시 알림 타입
     * @return BadgeType or null
     */
    private BadgeType getBadgeTypeForNotification(PushNotificationType type) {
        return switch (type) {
            case CAMPAIGN_END -> BadgeType.FIRST_CAMPAIGN;
            case FRIEND_ACCEPTED_RECEIVER, FRIEND_ACCEPTED_SENDER -> BadgeType.FRIEND_REGISTER;
            default -> null;
        };
    }

    /**
     * 배지 관련 세부 응답 생성
     *
     * @param memberId 배지를 발급받을 사용자 아이디
     * @param badgeType 발급 받을 배지 타입
     * @return FCMBadgeDetailResponse
     */
    @Transactional
    public FCMBadgeDetailResponse createBadgeDetailResponse(Long memberId, BadgeType badgeType) {
        boolean isBadgeEarned = memberBadgeService.earnBadge(memberId, badgeType);

        if (isBadgeEarned) {
            String badgeUrl = badgeRepository.findByActiveBadgeUrl(badgeType);
            return FCMBadgeDetailResponse.of(
                    isBadgeEarned,
                    badgeUrl,
                    MessageSourceUtils.getBadgeName(badgeType)
            );
        } else {
            return FCMBadgeDetailResponse.from(isBadgeEarned);
        }
    }
}
