package com.keypoint.keypointtravel.global.entity;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;

@MappedSuperclass
@Getter
@AllArgsConstructor
public abstract class LanguageEntity extends BaseEntity{
    private LanguageCode languageCode;
}
