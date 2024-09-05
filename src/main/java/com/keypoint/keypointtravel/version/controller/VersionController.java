package com.keypoint.keypointtravel.version.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.version.dto.response.VersionResponse;
import com.keypoint.keypointtravel.version.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
