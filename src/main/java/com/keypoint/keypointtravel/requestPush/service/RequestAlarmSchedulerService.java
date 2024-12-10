package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.enumType.notification.MarketingNotificationType;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.notification.event.marketingNotification.AdminMarketingNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.AdminPushNotificationEvent;
import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import com.keypoint.keypointtravel.requestPush.repository.RequestPushRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestAlarmSchedulerService {

    private final RequestPushRepository requestPushRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    //@Scheduled(cron = "0 0/30 * * * *")
    @Scheduled(cron = "0 0/2 * * * *")
    public void sendRequestAlarms() {
        try {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

            // 1. 전송해야 하는 RequestPush 조회
            List<RequestAlarm> alarms = requestPushRepository.findAllByReservationAt(now);
            for (RequestAlarm alarm : alarms) {
                if (alarm.getType() == RequestAlarmType.PUSH) {
                    sendRequestPushAlarms(alarm);
                } else {
                    sendRequestMarketingAlarms(alarm);
                }
            }

            LogUtils.writeInfoLog("sendRequestAlarms",
                "Send request pushes " + alarms.size());
        } catch (Exception ex) {
            LogUtils.writeErrorLog("sendRequestAlarms",
                "Fail to request pushes", ex);
        }
    }

    @Transactional
    public void sendRequestPushAlarms(RequestAlarm alarm) {
        // 1-1. 알림 전송 대상 조회
        List<Long> memberIds = memberRepository.findMemberIdByLanguageAndRole(
            alarm.getLanguageCode(),
            alarm.getRoleType()
        );

        // 1.2. 알림 전송
        eventPublisher.publishEvent(AdminPushNotificationEvent.of(
            PushNotificationType.PUSH_NOTIFICATION_BY_ADMIN,
            alarm.getTitle(),
            alarm.getContent(),
            memberIds
        ));
    }

    @Transactional
    public void sendRequestMarketingAlarms(RequestAlarm alarm) {
        // 1-1. 알림 전송 대상 조회
        List<String> memberEmails = memberRepository.findEmailByLanguageAndRole(
            alarm.getLanguageCode(),
            alarm.getRoleType()
        );

        // 1.2. 알림 전송
        Map<String, String> contents = new HashMap<>();
        contents.put("dynamicHtmlContent", alarm.getContent());
        eventPublisher.publishEvent(AdminMarketingNotificationEvent.of(
            MarketingNotificationType.ADMIN,
            contents,
            memberEmails,
            alarm.getTitle()
        ));
    }
}
