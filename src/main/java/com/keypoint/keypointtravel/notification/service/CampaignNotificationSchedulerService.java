package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.campaign.dto.useCase.AlarmCampaignUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.utils.DateUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignNotificationSchedulerService {

    private final CampaignRepository campaignRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 31 * * * *", zone = "Asia/Seoul")
    //@Scheduled(cron = "0 0 0 * * *")
    public void sendCampaignDMinus7Notification() {
        // 캠페인
        LocalDate compareDate = LocalDate.now().minusDays(7);
        List<AlarmCampaignUseCase> useCases = campaignRepository.findAlarmCampaignByStartAt(
            DateUtils.convertLocalDateToDate(compareDate)
        );

        for (AlarmCampaignUseCase useCase : useCases) {
            eventPublisher.publishEvent(
                CommonPushNotificationEvent.of(
                    PushNotificationType.CAMPAIGN_D_MINUS_7,
                    useCase.getMemberIds()
                )
            );
        }

        LogUtils.writeInfoLog("sendCampaignDMinus7Notification",
            "Send notifications 7 days before campaign starts " + useCases.size());
    }
}
