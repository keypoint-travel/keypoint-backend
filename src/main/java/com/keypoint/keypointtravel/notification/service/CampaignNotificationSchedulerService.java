package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.campaign.dto.useCase.AlarmCampaignUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.utils.DateUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignNotificationSchedulerService {

    private final CampaignRepository campaignRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void sendCampaignDMinus7Notification() {
        try {
            LocalDate compareDate = LocalDate.now().minusDays(7);
            int result = sendCampaignNotificationOnStartAt(
                PushNotificationType.CAMPAIGN_D_MINUS_7,
                DateUtils.convertLocalDateToDate(compareDate)
            );

            LogUtils.writeInfoLog("sendCampaignDMinus7Notification",
                "Send notifications 7 days before campaign starts " + result);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("sendCampaignDMinus7Notification",
                "Fail to send notifications 7 days before campaign starts", ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 02 * * * *", zone = "Asia/Seoul")
    //@Scheduled(cron = "0 0 0 * * *")
    public void sendCampaignDMinus1Notification() {
        LocalDate compareDate = LocalDate.now().minusDays(1);
        int result = sendCampaignNotificationOnStartAt(
            PushNotificationType.CAMPAIGN_D_DAY,
            DateUtils.convertLocalDateToDate(compareDate)
        );

        LogUtils.writeInfoLog("sendCampaignDMinus1Notification",
            "Send notifications 1 days before campaign starts " + result);
    }

    /**
     * 특정 날짜가 캠페인 시작날짜인 캠페인 참여자에게 알림을 전송하는 함수
     *
     * @param pushNotificationType 알림 타입
     * @param startAt              캠페인 시작 날짜
     * @return 알림이 전송된 캠페인의 수
     */
    private int sendCampaignNotificationOnStartAt(
        PushNotificationType pushNotificationType,
        Date startAt
    ) {
        List<AlarmCampaignUseCase> useCases = campaignRepository.findAlarmCampaignByStartAt(
            startAt
        );

        for (AlarmCampaignUseCase useCase : useCases) {
            eventPublisher.publishEvent(
                CommonPushNotificationEvent.of(
                    pushNotificationType,
                    useCase.getMemberIds()
                )
            );
        }

        return useCases.size();
    }
}
