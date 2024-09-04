package com.keypoint.keypointtravel.notice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice_detail_image")
@Getter
@NoArgsConstructor
public class NoticeDetailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_content_id")
    private NoticeContent noticeContent;

    @Column(name = "detail_image")
    private Long detailImageId;

    @Builder
    public NoticeDetailImage(NoticeContent noticeContent, Long detailImageId) {
        this.noticeContent = noticeContent;
        this.detailImageId = detailImageId;
    }

    public void setNoticeContent(NoticeContent noticeContent) {
        this.noticeContent = noticeContent;
    }
}

