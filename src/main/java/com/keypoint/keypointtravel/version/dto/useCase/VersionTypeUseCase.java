package com.keypoint.keypointtravel.version.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VersionTypeUseCase {
    private VersionType type;

    public static VersionTypeUseCase from(VersionType type) {
        return new VersionTypeUseCase(type);
    }
}
