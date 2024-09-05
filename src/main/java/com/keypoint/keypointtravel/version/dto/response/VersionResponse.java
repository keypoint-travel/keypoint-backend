package com.keypoint.keypointtravel.version.dto.response;

import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VersionResponse {
    private VersionType type;
    private String version;
}
