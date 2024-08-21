package com.keypoint.keypointtravel.notification.event.marketingNotification;

import com.keypoint.keypointtravel.global.enumType.notification.MarketingNotificationType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MarketingNotificationEvent {

    private MarketingNotificationType marketingNotificationType;

    private List<String> memberEmails;

    public abstract Object getAdditionalData();
}
