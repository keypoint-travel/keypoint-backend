package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "banner_content")
public class BannerContent extends LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @Column(nullable = false)
    private String contentId;

    @Column(nullable = false)
    private String mainTitle;

    @Column(nullable = false)
    private String subTitle;

    @Column
    private String placeName;

    @Column
    private String thumbnailImage;

    @Column
    private String address1;

    @Column
    private String address2;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String cat1;

    @Column(nullable = false)
    private String cat2;

    @Column(nullable = false)
    private String cat3;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public BannerContent(LanguageCode languageCode, Banner banner, String contentId, String mainTitle, String subTitle,
                         String placeName, String thumbnailImage, String address1, String address2, String contentType,
                         String cat1, String cat2, String cat3, boolean isDeleted) {
        super(languageCode);
        this.banner = banner;
        this.contentId = contentId;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.placeName = placeName;
        this.thumbnailImage = thumbnailImage;
        this.address1 = address1;
        this.address2 = address2;
        this.contentType = contentType;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.isDeleted = isDeleted;
    }

    public BannerContent() {
        super(LanguageCode.KO);
    }
}
