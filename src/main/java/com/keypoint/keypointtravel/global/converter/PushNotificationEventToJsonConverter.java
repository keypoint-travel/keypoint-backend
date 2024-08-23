package com.keypoint.keypointtravel.global.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            return objectMapper.readValue(dbData, PushNotificationEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
