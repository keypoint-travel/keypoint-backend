package com.keypoint.keypointtravel.guide.entity;

import com.keypoint.keypointtravel.global.entity.LanguageEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "guide_translation")
public class GuideTranslation extends LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_translation_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public GuideTranslation(
        String title,
        String subTitle,
        String content,
        LanguageCode languageCode
    ) {
        super(languageCode);
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.isDeleted = false;
    }
}
