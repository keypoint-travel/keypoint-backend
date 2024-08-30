package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import java.util.Set;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        Set<BadgeType> existedTypes = badgeRepository.findBadgeTypeByIsDeletedFalse();
    }
}
