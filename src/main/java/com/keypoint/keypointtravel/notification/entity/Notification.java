package com.keypoint.keypointtravel.notification.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @Comment("푸쉬 알림 가능 여부")
    private boolean pushNotificationEnabled;

    @Column(nullable = false)
    @Comment("마켓팅 알림 가능 여부")
    private boolean marketingNotificationEnabled;

    public Notification(Member member) {
        this.member = member;
        this.pushNotificationEnabled = true;
        this.marketingNotificationEnabled = true;
    }

    public static Notification from(Member member) {
        return new Notification(member);
    }
}
