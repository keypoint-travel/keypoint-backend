package com.keypoint.keypointtravel.global.entity;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;

@MappedSuperclass
@Getter
@AllArgsConstructor
public abstract class LanguageEntity extends BaseEntity{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
}
