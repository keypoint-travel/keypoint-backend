package com.keypoint.keypointtravel.notification.event.marketingNotification;

import com.keypoint.keypointtravel.global.enumType.notification.MarketingNotificationType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MarketingNotificationEvent {

    private MarketingNotificationType marketingNotificationType;

    private Map<String, String> emailContent;

    private List<String> memberEmails;

    public abstract Object getAdditionalData();
}
