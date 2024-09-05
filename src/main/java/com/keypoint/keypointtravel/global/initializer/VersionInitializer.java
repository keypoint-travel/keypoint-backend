package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.version.entity.Version;
import com.keypoint.keypointtravel.version.repository.VersionRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class VersionInitializer {
    private final VersionRepository versionRepository;

    public VersionInitializer(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateVersions() {
        try {
            Set<VersionType> existedTypes = versionRepository.findVersionType();
            Set<VersionType> availableTypes = new HashSet<>(EnumSet.allOf(VersionType.class));
            availableTypes.removeAll(existedTypes);

            // 존재하지 않는 Version 만 생성
            List<Version> versions = new ArrayList<>();
            for (VersionType type : availableTypes) {
                Version newVersion = new Version(type);

                versions.add(newVersion);
            }

            versionRepository.saveAll(versions);
        } catch (Exception e) {
            LogUtils.writeErrorLog("generateVersions", "Failed to generate versions " + e.getMessage());
        }
    }

}
