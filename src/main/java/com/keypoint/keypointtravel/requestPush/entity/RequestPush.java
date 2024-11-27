package com.keypoint.keypointtravel.requestPush.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "request_push")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPush extends BaseEntity {

    @Id
    @Column(name = "request_push_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public RequestPush(
        String title,
        String content,
        LanguageCode languageCode,
        LocalDateTime reservationAt,
        RoleType roleType
    ) {
        this.title = title;
        this.content = content;
        this.languageCode = languageCode;
        this.reservationAt = reservationAt.withSecond(0).withNano(0);
        this.roleType = roleType;
    }
}
