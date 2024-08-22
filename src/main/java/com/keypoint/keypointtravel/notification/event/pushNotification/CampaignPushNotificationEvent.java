package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignPushNotificationEvent extends PushNotificationEvent {

    private CampaignData additionalData;

    public CampaignPushNotificationEvent() {
        super(null, null);
    }

    public CampaignPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        Long campaignId
    ) {
        super(type, memberIds);
        this.additionalData = new CampaignData(campaignId);
    }

    public static CampaignPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        Long campaignId
    ) {
        return new CampaignPushNotificationEvent(type, memberIds, campaignId);
    }

    @Override
    public CampaignData getAdditionalData() {
        return additionalData;
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CampaignData {

        @JsonProperty("campaignId") // JSON 매핑을 명시적으로 설정
        private Long campaignId;

        public CampaignData(Long campaignId) {
            this.campaignId = campaignId;
        }

        public Long getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(Long campaignId) { // Setter 추가 (필요 시)
            this.campaignId = campaignId;
        }
    }
}
