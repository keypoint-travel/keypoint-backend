package com.keypoint.keypointtravel.version.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import com.keypoint.keypointtravel.version.dto.request.UpdateVersionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateVersionUseCase {
    String version;
    private VersionType type;

    public static UpdateVersionUseCase of(
            VersionType type,
            UpdateVersionRequest request
    ) {
        return new
                UpdateVersionUseCase(request.getVersion(), type);
    }
}
