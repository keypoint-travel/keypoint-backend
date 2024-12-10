package com.keypoint.keypointtravel.notification.event.marketingNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.MarketingNotificationType;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminMarketingNotificationEvent extends MarketingNotificationEvent {

    private String title;

    public AdminMarketingNotificationEvent(
        MarketingNotificationType marketingNotificationType,
        Map<String, String> emailContent,
        List<String> memberEmails,
        String title
    ) {
        super(marketingNotificationType, emailContent, memberEmails);
        this.title = title;
    }

    public static AdminMarketingNotificationEvent of(
        MarketingNotificationType marketingNotificationType,
        Map<String, String> emailContent,
        List<String> memberEmails,
        String title
    ) {
        return new AdminMarketingNotificationEvent(
            marketingNotificationType,
            emailContent,
            memberEmails,
            title
        );
    }

    @Override
    public Object getAdditionalData() {
        return null;
    }
}
