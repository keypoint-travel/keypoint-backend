package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "advertisement_banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_banner_id")
    private Long id;

    @Column
    private Long thumbnailImageId;

    @Column
    private Long detailImageId;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "advertisementBanner", orphanRemoval = true)
    private List<AdvertisementBannerContent> bannerContents = new ArrayList<>();

    public AdvertisementBanner(Long thumbnailImageId, Long detailImageId) {
        this.thumbnailImageId = thumbnailImageId;
        this.detailImageId = detailImageId;
        this.isDeleted = false;
    }

    public void updateBanner(Long thumbnailImageId, Long detailImageId){
        this.thumbnailImageId = thumbnailImageId;
        this.detailImageId = detailImageId;
    }
}
