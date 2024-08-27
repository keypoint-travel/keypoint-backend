package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import org.springframework.stereotype.Component;

@Component
public class BadgeInitializer {

    private final AdminBadgeService adminBadgeService;

    public BadgeInitializer(AdminBadgeService adminBadgeService) {
        this.adminBadgeService = adminBadgeService;
    }
}
