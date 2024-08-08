package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "advertisement_banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(nullable = false)
    private String mainTitle;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    private String content;

    @Column
    private Long thumbnailImageId;

    @Column
    private Long detailImageId;

    @Column
    private boolean isExposed;

    public AdvertisementBanner(String mainTitle, String subTitle, String content, Long thumbnailImageId, Long detailImageId) {
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnailImageId = thumbnailImageId;
        this.detailImageId = detailImageId;
        this.isExposed = true;
    }
}
