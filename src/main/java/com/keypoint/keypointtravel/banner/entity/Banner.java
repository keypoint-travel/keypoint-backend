package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @Column(name = "banner_id")
    private Long id;

    @Column
    private String name;

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
    private String mainTitle;

    @Column(nullable = false)
    private String subTitle;

    @Column
    private String thumbnailImage;

    @Column
    private String address1;

    @Column
    private String address2;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private boolean isExposed; // 사용자에게 노출 여부

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    private List<BannerComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    private List<BannerLike> bannerLikes = new ArrayList<>();

    @Builder
    public Banner(Long id, String name, AreaCode areaCode, LargeCategory cat1, MiddleCategory cat2, SmallCategory cat3,
                  ContentType contentType, String mainTitle, String subTitle, String thumbnailImage, String address1, String address2,
                  Double latitude, Double longitude, boolean isExposed) {
        this.id = id;
        this.name = name;
        this.areaCode = areaCode;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contentType = contentType;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.thumbnailImage = thumbnailImage;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isExposed = isExposed;
    }
}