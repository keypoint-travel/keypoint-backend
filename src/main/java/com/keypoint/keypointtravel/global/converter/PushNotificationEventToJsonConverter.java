package com.keypoint.keypointtravel.global.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignApplicantPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignLeaderPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaingAcceptorPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.FriendPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PushNotificationEventToJsonConverter implements
    AttributeConverter<PushNotificationEvent, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PushNotificationEvent entityData) {
        try {
            return objectMapper.writeValueAsString(entityData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PushNotificationEvent convertToEntityAttribute(String dbData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(dbData);
            // "pushNotificationType" 필드의 값을 추출하여 enum으로 변환
            String typeText = jsonNode.get("pushNotificationType").asText();
            PushNotificationType pushNotificationType = PushNotificationType.valueOf(typeText);
            switch (pushNotificationType) {
                case RECEIPT_REGISTER,
                     CAMPAIGN_D_MINUS_7,
                     CAMPAIGN_D_DAY,
                     CAMPAIGN_NO_EXPENSE_D1,
                     PAYMENT_COMPLETION,
                     INQUIRY_RESPONSE_COMPLETED,
                     CAMPAIGN_D60_PASSED -> {
                    return objectMapper.readValue(dbData, CommonPushNotificationEvent.class);
                }
                case CAMPAIGN_INVITE, CAMPAIGN_ACCEPT_INVITEE -> {
                    return objectMapper.readValue(dbData,
                        CampaignLeaderPushNotificationEvent.class);
                }
                case CAMPAIGN_ACCEPT_INVITER -> {
                    return objectMapper.readValue(dbData,
                        CampaingAcceptorPushNotificationEvent.class);
                }
                case CAMPAIGN_END -> {
                    return objectMapper.readValue(dbData, CampaignPushNotificationEvent.class);
                }
                case CAMPAIGN_JOIN_REQUEST -> {
                    return objectMapper.readValue(dbData,
                        CampaignApplicantPushNotificationEvent.class);
                }
                case FRIEND_ADDED, FRIEND_ACCEPTED_RECEIVER, FRIEND_ACCEPTED_SENDER -> {
                    return objectMapper.readValue(dbData, FriendPushNotificationEvent.class);
                }
            }

            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
