package com.keypoint.keypointtravel.notice.Entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private String noticeImageUrl;

    @Column
    private boolean isDeleted;

    @Builder
    public Notice(String title, String content, String noticeImageUrl, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.noticeImageUrl = noticeImageUrl;
        this.isDeleted = isDeleted;
    }
}