package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AreaCode areaCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LargeCategory cat1;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MiddleCategory cat2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SmallCategory cat3;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private String thumbnailTitle;

    @Column
    private String thumbnailImage;

    @Column(nullable = false)
    private boolean isExposed;

    public Banner(Long id, AreaCode areaCode, LargeCategory cat1, MiddleCategory cat2,
        SmallCategory cat3, ContentType contentType, String thumbnailTitle, String thumbnailImage,
        boolean isExposed) {
        this.id = id;
        this.areaCode = areaCode;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contentType = contentType;
        this.thumbnailTitle = thumbnailTitle;
        this.thumbnailImage = thumbnailImage;
        this.isExposed = isExposed;
    }
}