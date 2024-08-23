package com.keypoint.keypointtravel.notification.entity;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.converter.PushNotificationEventToJsonConverter;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "push_notification_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushNotificationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_notification_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PushNotificationContent type;

    @Column(nullable = false, name = "is_read")
    private boolean isRead;

    @Convert(converter = PushNotificationEventToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private PushNotificationEvent detailData;

    public PushNotificationHistory(
        PushNotificationContent type,
        Member member,
        Campaign campaign,
        PushNotificationEvent detailData
    ) {
        this.member = member;
        this.campaign = campaign;
        this.type = type;
        this.isRead = false;
        this.detailData = detailData;
    }

    public static PushNotificationHistory of(
        PushNotificationContent type,
        Member member,
        Campaign campaign,
        PushNotificationEvent detailData
    ) {
        return new PushNotificationHistory(type, member, campaign, detailData);
    }
}
