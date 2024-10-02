package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.badge.dto.useCase.CreateBadgeUseCase;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.global.utils.MultiPartFileUtils;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BadgeInitializer {

    private final AdminBadgeService adminBadgeService;
    private final BadgeRepository badgeRepository;

    public BadgeInitializer(
        AdminBadgeService adminBadgeService, BadgeRepository badgeRepository) {
        this.adminBadgeService = adminBadgeService;
        this.badgeRepository = badgeRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateBadges() {
        try {
            Set<BadgeType> existedTypes = badgeRepository.findBadgeTypeByIsDeletedFalse();
            Set<BadgeType> availableTypes = new HashSet<>(EnumSet.allOf(BadgeType.class));
            availableTypes.removeAll(existedTypes);

            // 존재하지 않는 Badge 만 생성
            for (BadgeType type : availableTypes) {
                CreateBadgeUseCase useCase = null;
                switch (type) {
                    case SIGN_UP -> {
                        MultipartFile activeFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "signup_active.png");
                        MultipartFile inactiveFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "signup_inactive.png");
                        useCase = new CreateBadgeUseCase(
                            type,
                            activeFile,
                            inactiveFile
                        );
                    }
                    case FIRST_CAMPAIGN -> {
                        MultipartFile activeFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "campaign_active.png");
                        MultipartFile inactiveFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "campaign_inactive.png");
                        useCase = new CreateBadgeUseCase(
                            type,
                            activeFile,
                            inactiveFile
                        );
                    }
                    case FRIEND_REGISTER -> {
                        MultipartFile activeFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "friend_active.png");
                        MultipartFile inactiveFile = MultiPartFileUtils.getImageAsMultipartFile(
                            "friend_inactive.png");
                        useCase = new CreateBadgeUseCase(
                            type,
                            activeFile,
                            inactiveFile
                        );
                    }
                    default -> {
                        continue;
                    }
                }

                adminBadgeService.addBadge(useCase);
            }
        } catch (Exception e) {
            LogUtils.writeErrorLog("generateBadges", "Failed to generate badges " + e.getMessage());
        }
    }
}
