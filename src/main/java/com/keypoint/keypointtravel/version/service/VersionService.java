package com.keypoint.keypointtravel.version.service;

import com.keypoint.keypointtravel.version.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;
}
