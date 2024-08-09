package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "advertisement_banner_content")
public class AdvertisementBannerContent extends LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_banner_content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_banner_id")
    private AdvertisementBanner advertisementBanner;

    @Column(nullable = false)
    private String mainTitle;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    private String content;

    @Column
    private boolean isDeleted;

    @Builder
    public AdvertisementBannerContent(LanguageCode languageCode, AdvertisementBanner advertisementBanner,
                                      String mainTitle, String subTitle, String content) {
        super(languageCode);
        this.advertisementBanner = advertisementBanner;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.content = content;
        this.isDeleted = false;
    }

    public AdvertisementBannerContent() {
        super(LanguageCode.KO);
    }
}
