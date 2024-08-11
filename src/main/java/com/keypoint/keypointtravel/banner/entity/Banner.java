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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AreaCode areaCode;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    private List<BannerContent> bannerContents = new ArrayList<>();

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    private List<BannerComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "banner", orphanRemoval = true)
    private List<BannerLike> bannerLikes = new ArrayList<>();

    @Builder
    public Banner(AreaCode areaCode, Double latitude, Double longitude, boolean isDeleted) {
        this.areaCode = areaCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDeleted = isDeleted;
    }
}