package com.keypoint.keypointtravel.notice.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notice")
public class Notice extends LanguageEntity {

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
    private Long thumbnailImageId;

    @ElementCollection
    @CollectionTable(name = "notice_detail_images", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "detail_image")
    private List<Long> detailImageIds;

    @Column
    private boolean isDeleted;

    @Builder
    public Notice(
        LanguageCode languageCode, String title, String content, Long thumbnailImageId, List<Long> detailImageIds) {
        super(languageCode);
        this.title = title;
        this.content = content;
        this.detailImageIds = detailImageIds;
        this.thumbnailImageId = thumbnailImageId;
        this.isDeleted = false;
    }

    public Notice() {
        super(LanguageCode.KO);
    }
}