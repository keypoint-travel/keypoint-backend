package com.keypoint.keypointtravel.notice.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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
    private Long thumbnailImageId;

    @OneToMany(mappedBy = "noticeContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeDetailImage> detailImages;

    @Column
    private boolean isDeleted;

    @Builder
    public NoticeContent(Notice notice,
        LanguageCode languageCode, String title, String content, Long thumbnailImageId,List<NoticeDetailImage> detailImages) {
        super(languageCode);
        this.notice = notice;
        this.title = title;
        this.content = content;
        this.detailImages = detailImages;
        this.thumbnailImageId = thumbnailImageId;
        this.isDeleted = false;
    }

    public NoticeContent() {
        super(LanguageCode.KO);
    }

    public void setDetailImages(List<NoticeDetailImage> detailImages) {
        this.detailImages = detailImages;
        for (NoticeDetailImage detailImage : detailImages) {
            detailImage.setNoticeContent(this);
        }
    }

}
