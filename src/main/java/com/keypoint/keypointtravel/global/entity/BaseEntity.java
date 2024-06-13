package com.keypoint.keypointtravel.global.entity;

import com.keypoint.keypointtravel.global.converter.TimestampConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime modifyAt;

    @CreatedBy
    @Column(updatable = false)
    private String registerId;

    @LastModifiedBy
    private String modifyId;
}
