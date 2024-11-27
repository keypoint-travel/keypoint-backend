package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.notification.event.pushNotification.AdminPushNotificationEvent;
import com.keypoint.keypointtravel.requestPush.entity.RequestPush;
import com.keypoint.keypointtravel.requestPush.repository.RequestPushRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestPushSchedulerService {

    private final RequestPushRepository requestPushRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Scheduled(cron = "0 0/30 * * * *")
    public void sendRequestPushes() {
        try {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

            // 1. 전송해야 하는 RequestPush 조회
            List<RequestPush> pushes = requestPushRepository.findAllByReservationAt(now);
            for (RequestPush push : pushes) {
                // 1-1. 알림 전송 대상 조회
                List<Long> memberIds = memberRepository.findMemberIdByLanguageAndRole(
                    push.getLanguageCode(),
                    push.getRoleType()
                );

                // 1.2. 알림 전송
                eventPublisher.publishEvent(AdminPushNotificationEvent.of(
                    PushNotificationType.PUSH_NOTIFICATION_BY_ADMIN,
                    push.getTitle(),
                    push.getContent(),
                    memberIds
                ));
            }

            LogUtils.writeInfoLog("sendRequestPushes",
                "Send request pushes " + pushes.size());
        } catch (Exception ex) {
            LogUtils.writeErrorLog("sendRequestPushes",
                "Fail to request pushes", ex);
        }
    }
}
