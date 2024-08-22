package com.keypoint.keypointtravel.notification.entity;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationMsg;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private PushNotificationMsg type;

    @Column(nullable = false, name = "is_read")
    private boolean isRead;

    public PushNotificationHistory(
        PushNotificationMsg type,
        Member member,
        Campaign campaign
    ) {
        this.member = member;
        this.campaign = campaign;
        this.type = type;
        this.isRead = false;
    }

    public static PushNotificationHistory of(
        PushNotificationMsg type,
        Member member
    ) {
        return new PushNotificationHistory(type, member, null);
    }

    public static PushNotificationHistory of(
        PushNotificationMsg type,
        Member member,
        Campaign campaign
    ) {
        return new PushNotificationHistory(type, member, campaign);
    }
}
