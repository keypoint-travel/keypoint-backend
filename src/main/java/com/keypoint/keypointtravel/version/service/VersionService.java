package com.keypoint.keypointtravel.version.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.version.dto.response.VersionResponse;
import com.keypoint.keypointtravel.version.dto.useCase.UpdateVersionUseCase;
import com.keypoint.keypointtravel.version.dto.useCase.VersionTypeUseCase;
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

    /**
     * 버전 수정
     *
     * @return
     */
    @Transactional
    public void updateVersion(UpdateVersionUseCase useCase) {
        try {
            versionRepository.updateVersion(useCase.getVersion(), useCase.getType());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 특정 버전의 타입 조회
     *
     * @param useCase
     * @return
     */
    public VersionResponse findVersionByType(VersionTypeUseCase useCase) {
        try {
            return versionRepository.findVersionByType(useCase.getType());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
