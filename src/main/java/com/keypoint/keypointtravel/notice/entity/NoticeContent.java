package com.keypoint.keypointtravel.notice.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notice_content")
public class NoticeContent extends LanguageEntity {
    @Id
    @Column(name = "notice_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private boolean isDeleted;

    @Builder
    public NoticeContent(Notice notice,
        LanguageCode languageCode, String title, String content) {
        super(languageCode);
        this.notice = notice;
        this.title = title;
        this.content = content;
        this.isDeleted = false;
    }

    public NoticeContent() {
        super(LanguageCode.EN);
    }
}
