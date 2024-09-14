package com.keypoint.keypointtravel.notice.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoticeContent> noticeContents;

    @Column(nullable = false)
    private Long thumbnailImageId;

    @Column(nullable = false)
    private boolean isDeleted;

    public Notice(Long thumbnailImageId) {
        this.thumbnailImageId = thumbnailImageId;
        this.isDeleted = false;
    }

    public static Notice from(Long thumbnailImageId) {
        return new Notice(thumbnailImageId);
    }
}