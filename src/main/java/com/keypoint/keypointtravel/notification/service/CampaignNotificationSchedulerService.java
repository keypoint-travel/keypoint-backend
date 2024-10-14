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
            LocalDate compareDate = LocalDate.now().plusDays(7);
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
    @Scheduled(cron = "0 0 0 * * *")
    public void sendCampaignDMinus1Notification() {
        try {
            LocalDate compareDate = LocalDate.now().plusDays(1);
            int result = sendCampaignNotificationOnStartAt(
                PushNotificationType.CAMPAIGN_D_DAY,
                DateUtils.convertLocalDateToDate(compareDate)
            );

            LogUtils.writeInfoLog("sendCampaignDMinus1Notification",
                "Send notifications 1 days before campaign starts " + result);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("sendCampaignDMinus1Notification",
                "Fail to send notifications 1 days before campaign starts", ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void sendCampaignNoExpenseD1Notification() {
        try {
            LocalDate compareDate = LocalDate.now().minusDays(1);
            List<AlarmCampaignUseCase> useCases = campaignRepository.findAlarmCampaignByStartAtAndNoExpense(
                DateUtils.convertLocalDateToDate(compareDate)
            );

            for (AlarmCampaignUseCase useCase : useCases) {
                eventPublisher.publishEvent(
                    CommonPushNotificationEvent.of(
                        PushNotificationType.CAMPAIGN_NO_EXPENSE_D1,
                        useCase.getMemberIds()
                    )
                );
            }

            LogUtils.writeInfoLog("sendCampaignNoExpenseD1Notification",
                "Send notifications 1 days after campaign starts, if there is no spending "
                    + useCases.size());
        } catch (Exception ex) {
            LogUtils.writeErrorLog("sendCampaignNoExpenseD1Notification",
                "Fail to send notifications 1 days after campaign starts, if there is no spending",
                ex);
        }
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
//
//    @Transactional
//    @Scheduled(cron = "0 23 * * * *", zone = "Asia/Seoul")
//    //@Scheduled(cron = "0 0 0 * * *")
//    public void sendCampaignD60PassedNotification() {
//        LocalDate compareDate = LocalDate.now().minusDays(60);
//        List<AlarmCampaignUseCase> useCases = campaignRepository.findAlarmCampaignByEndAt(
//            DateUtils.convertLocalDateToDate(compareDate)
//        );
//
//        // 60일이 지난 캠페인 조회
//        for (AlarmCampaignUseCase useCase : useCases) {
////            eventPublisher.publishEvent(
////                CommonPushNotificationEvent.of(
////                    PushNotificationType.CAMPAIGN_D60_PASSED,
////                    useCase.getMemberIds()
////                )
////            );
//        }
//
//        // 사용자들 중 조회한 캠페인이 마지막인 경우 알림 전달
//
//        LogUtils.writeInfoLog("sendCampaignD60PassedNotification",
//            "Send notifications when the last campaign is past 60 days "
//                + useCases.size());
//    }
}
