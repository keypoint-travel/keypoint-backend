package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "banner")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @Column(name = "banner_id")
    private Long id;

    @Column
    private String title;

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

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private boolean isExposed;
}