package com.keypoint.keypointtravel.global.enumType.notification;

import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationContent {
    RECEIPT_REGISTER(
        PushNotificationType.RECEIPT_REGISTER,
        "pushNotification.receiptRegister_title",
        "pushNotification.receiptRegister_content"
    ),
    CAMPAIGN_D_MINUS_7_1(
        PushNotificationType.CAMPAIGN_D_MINUS_7,
        "pushNotification.campaignDMinus7_1_title",
        "pushNotification.campaignDMinus7_1_content"
    ),
    CAMPAIGN_D_MINUS_7_2(
        PushNotificationType.CAMPAIGN_D_MINUS_7,
        "pushNotification.campaignDMinus7_2_title",
        "pushNotification.campaignDMinus7_2_content"
    ),
    CAMPAIGN_D_MINUS_7_3(
        PushNotificationType.CAMPAIGN_D_MINUS_7,
        "pushNotification.campaignDMinus7_3_title",
        "pushNotification.campaignDMinus7_3_content"
    ),
    CAMPAIGN_D_MINUS_7_4(
        PushNotificationType.CAMPAIGN_D_MINUS_7,
        "pushNotification.campaignDMinus7_4_title",
        "pushNotification.campaignDMinus7_4_content"
    ),
    CAMPAIGN_D_MINUS_7_5(
        PushNotificationType.CAMPAIGN_D_MINUS_7,
        "pushNotification.campaignDMinus7_5_title",
        "pushNotification.campaignDMinus7_5_content"
    ),
    CAMPAIGN_D_DAY_1(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay1_title",
        "pushNotification.campaignDDay1_content"
    ),
    CAMPAIGN_D_DAY_2(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay2_title",
        "pushNotification.campaignDDay2_content"
    ),
    CAMPAIGN_D_DAY_3(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay3_title",
        "pushNotification.campaignDDay3_content"
    ),
    CAMPAIGN_D_DAY_4(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay4_title",
        "pushNotification.campaignDDay4_content"
    ),
    CAMPAIGN_D_DAY_5(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay5_title",
        "pushNotification.campaignDDay5_content"
    ),
    CAMPAIGN_INVITE(
        PushNotificationType.CAMPAIGN_INVITE,
        "pushNotification.campaignInvite_title",
        "pushNotification.campaignInvite_content"
    ),
    CAMPAIGN_ACCEPT_INVITER(
        PushNotificationType.CAMPAIGN_ACCEPT_INVITER,
        "pushNotification.campaignAcceptInviter_title",
        "pushNotification.campaignAcceptInviter_content"
    ),

    CAMPAIGN_ACCEPT_INVITEE(
        PushNotificationType.CAMPAIGN_ACCEPT_INVITEE,
        "pushNotification.campaignAcceptInvitee_title",
        "pushNotification.campaignAcceptInvitee_content"
    ),
    CAMPAIGN_NO_EXPENSE_D1(
        PushNotificationType.CAMPAIGN_NO_EXPENSE_D1,
        "pushNotification.campaignNoExpenseD1_title",
        "pushNotification.campaignNoExpenseD1_content"
    ),
    CAMPAIGN_END(
        PushNotificationType.CAMPAIGN_END,
        "pushNotification.campaignEnd_title",
        "pushNotification.campaignEnd_content"
    ),
    CAMPAIGN_JOIN_REQUEST(
        PushNotificationType.CAMPAIGN_JOIN_REQUEST,
        "pushNotification.campaignJoinRequest_title",
        "pushNotification.campaignJoinRequest_content"
    ),
    PAYMENT_COMPLETION(
        PushNotificationType.PAYMENT_COMPLETION,
        "pushNotification.paymentCompleted_title",
        "pushNotification.paymentCompleted_content"
    ),
    FRIEND_ADDED(
        PushNotificationType.FRIEND_ADDED,
        "pushNotification.friendAdded_title",
        "pushNotification.friendAdded_content"
    ),
    FRIEND_ACCEPTED_RECEIVER(
        PushNotificationType.FRIEND_ACCEPTED_RECEIVER,
        "pushNotification.friendAcceptedReceiver_title",
        "pushNotification.friendAcceptedReceiver_content"
    ),
    FRIEND_ACCEPTED_SENDER(
        PushNotificationType.FRIEND_ACCEPTED_SENDER,
        "pushNotification.friendAcceptedSender_title",
        "pushNotification.friendAcceptedSender_content"
    ),
    INQUIRY_RESPONSE_COMPLETED(
        PushNotificationType.INQUIRY_RESPONSE_COMPLETED,
        "pushNotification.inquiryResponseCompleted_title",
        "pushNotification.inquiryResponseCompleted_content"
    ),
    CAMPAIGN_D60_PASSED_1(
        PushNotificationType.CAMPAIGN_D60_PASSED,
        "pushNotification.campaignD60Passed1_title",
        "pushNotification.campaignD60Passed1_content"),
    CAMPAIGN_D60_PASSED_2(
        PushNotificationType.CAMPAIGN_D60_PASSED,
        "pushNotification.campaignD60Passed2_title",
        "pushNotification.campaignD60Passed2_content"
    );

    private final PushNotificationType type;
    private final String titleLangCode;
    private final String contentLangCode;

    public static PushNotificationContent getRandomNotificationContent(PushNotificationType type) {
        // 1. 가능한 문구 조회
        List<PushNotificationContent> msgs = getNotificationMsgs(type);
        if (msgs.isEmpty()) {
            return null;
        } else if (msgs.size() == 1) {
            return msgs.get(0);
        }

        // 2. 가능한 문구 중 하나 반환
        Random random = new Random();
        int randomIndex = random.nextInt(msgs.size());
        return msgs.get(randomIndex);
    }

    public static List<PushNotificationContent> getNotificationMsgs(PushNotificationType type) {
        List<PushNotificationContent> notificationMsgs = new ArrayList<>();
        for (PushNotificationContent notificationMsg : values()) {
            if (notificationMsg.type.equals(type)) {
                notificationMsgs.add(notificationMsg);
            }
        }

        return notificationMsgs;
    }

    public String getTranslatedTitle(String name, String campaignName, Locale locale) {
        switch (this) {
            case FRIEND_ACCEPTED_SENDER:
            case CAMPAIGN_ACCEPT_INVITER:
                return MessageSourceUtils.getLocalizedLanguageWithVariables(contentLangCode,
                    new Object[]{name}, locale);
        }
        return null;
    }

    public String getTranslatedContent(String name, String campaignName, Locale locale) {
        switch (this) {
            case RECEIPT_REGISTER:
            case FRIEND_ADDED:
            case FRIEND_ACCEPTED_RECEIVER:
            case FRIEND_ACCEPTED_SENDER:
                return MessageSourceUtils.getLocalizedLanguageWithVariables(contentLangCode,
                    new Object[]{name}, locale);
            case CAMPAIGN_D_MINUS_7_1:
            case CAMPAIGN_D_MINUS_7_2:
            case CAMPAIGN_D_MINUS_7_3:
            case CAMPAIGN_D_MINUS_7_4:
            case CAMPAIGN_D_MINUS_7_5:
            case CAMPAIGN_D_DAY_1:
            case CAMPAIGN_D_DAY_2:
            case CAMPAIGN_D_DAY_3:
            case CAMPAIGN_D_DAY_4:
            case CAMPAIGN_D_DAY_5:
            case CAMPAIGN_NO_EXPENSE_D1:
            case PAYMENT_COMPLETION:
            case INQUIRY_RESPONSE_COMPLETED:
            case CAMPAIGN_D60_PASSED_1:
            case CAMPAIGN_D60_PASSED_2:
                return MessageSourceUtils.getLocalizedLanguage(contentLangCode, locale);
            case CAMPAIGN_INVITE:
            case CAMPAIGN_ACCEPT_INVITEE:
            case CAMPAIGN_JOIN_REQUEST:
                return MessageSourceUtils.getLocalizedLanguageWithVariables(
                    contentLangCode,
                    new Object[]{name, campaignName},
                    locale);
            case CAMPAIGN_ACCEPT_INVITER:
            case CAMPAIGN_END:
                return MessageSourceUtils.getLocalizedLanguageWithVariables(
                    contentLangCode,
                    new Object[]{campaignName},
                    locale);
        }

        return null;
    }
}
