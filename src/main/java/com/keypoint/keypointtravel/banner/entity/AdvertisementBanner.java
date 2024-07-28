package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "advertisement_banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementBanner extends BaseEntity {

    @Id
    @Column(name = "banner_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Long thumbnailImageId;

    @Column
    private Long detailImageId;

    public AdvertisementBanner(String title, String content, Long thumbnailImageId, Long detailImageId) {
        this.title = title;
        this.content = content;
        this.thumbnailImageId = thumbnailImageId;
        this.detailImageId = detailImageId;
    }
}
