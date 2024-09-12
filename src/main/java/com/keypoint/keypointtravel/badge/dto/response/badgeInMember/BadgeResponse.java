package com.keypoint.keypointtravel.badge.dto.response.badgeInMember;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.badge.entity.EarnedBadge;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@NoArgsConstructor
public class BadgeResponse {

    private Long badgeId;
    private String name;
    private int order;
    private String badgeImageUrl;
    private boolean isEarned;

    @QueryProjection
    public BadgeResponse(
        Badge badge,
        EarnedBadge earnedBadge,
        String activeImageUrl,
        String inactiveImageUrl
    ) {
        this.badgeId = badge.getId();
        this.order = badge.getOrder();

        if (earnedBadge != null) {
            this.badgeImageUrl = activeImageUrl;
            this.isEarned = true;
        } else {
            this.badgeImageUrl = inactiveImageUrl;
            this.isEarned = false;
        }

        setName(badge.getType());
    }

    private void setName(BadgeType badgeType) {
        try {
            Locale currentLocale = LocaleContextHolder.getLocale();
            String languageKey = badgeType.getLanguageKey();
            this.name = MessageSourceUtils.getLocalizedLanguage(
                    languageKey,
                    currentLocale
            );
        } catch (Exception ex) {
            LogUtils.writeErrorLog("BadgeResponse", "Fail to get language " + ex.getMessage());
            this.name = badgeType.getDescription();
        }
    }
}
