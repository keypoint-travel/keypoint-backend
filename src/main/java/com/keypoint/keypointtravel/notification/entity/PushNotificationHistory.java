package com.keypoint.keypointtravel.notification.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private PushNotificationType type;

    @Column(nullable = false)
    private boolean isRead;

    public PushNotificationHistory(
        String title,
        String content,
        PushNotificationType type,
        Member member
    ) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = false;
    }

    public static PushNotificationHistory of(
        String title,
        String content,
        PushNotificationType type,
        Member member
    ) {
        return new PushNotificationHistory(title, content, type, member);
    }
}
