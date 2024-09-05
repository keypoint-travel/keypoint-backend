package com.keypoint.keypointtravel.version.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.version.dto.response.VersionResponse;
import com.keypoint.keypointtravel.version.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;

    /**
     * 모든 타입에 대한 버전 정보 조회
     *
     * @return
     */
    public List<VersionResponse> findVersions() {
        try {
            return versionRepository.findCommonVersions();
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
