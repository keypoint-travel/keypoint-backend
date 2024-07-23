package com.keypoint.keypointtravel.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Converter(autoApply = true)
public class TimestampConverter implements AttributeConverter<LocalDateTime, Date> {

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Override
    public Date convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute == null ? null : Date.from(attribute.atZone(UTC).toInstant());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date dbData) {
        return dbData == null ? null
            : LocalDateTime.ofInstant(Instant.ofEpochMilli(dbData.getTime()), UTC);
    }
}
