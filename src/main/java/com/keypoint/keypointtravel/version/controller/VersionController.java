package com.keypoint.keypointtravel.version.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import com.keypoint.keypointtravel.version.dto.request.UpdateVersionRequest;
import com.keypoint.keypointtravel.version.dto.response.VersionResponse;
import com.keypoint.keypointtravel.version.dto.useCase.UpdateVersionUseCase;
import com.keypoint.keypointtravel.version.dto.useCase.VersionTypeUseCase;
import com.keypoint.keypointtravel.version.service.VersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/versions")
public class VersionController {
    private final VersionService versionService;

    @GetMapping("/management")
    public APIResponseEntity<List<VersionResponse>> findVersions() {
        List<VersionResponse> result = versionService.findVersions();

        return APIResponseEntity.<List<VersionResponse>>builder()
                .message("전체 버전 정보 조회")
                .data(result)
                .build();
    }

    @PutMapping()
    public APIResponseEntity<Void> updateVersion(
            @RequestParam(value = "type") VersionType type,
            @Valid @RequestBody UpdateVersionRequest request
    ) {
        UpdateVersionUseCase useCase = UpdateVersionUseCase.of(type, request);
        versionService.updateVersion(useCase);

        return APIResponseEntity.<Void>builder()
                .message("버전 정보 수정")
                .build();
    }

    @GetMapping()
    public APIResponseEntity<VersionResponse> findVersionByType(
            @RequestParam(value = "type") VersionType type
    ) {
        VersionTypeUseCase useCase = VersionTypeUseCase.from(type);
        VersionResponse result = versionService.findVersionByType(useCase);

        return APIResponseEntity.<VersionResponse>builder()
                .message("버전 정보 조회")
                .data(result)
                .build();
    }
}
