package com.keypoint.keypointtravel.notification.listener;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.FCMUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.fcmToken.FCMTokenRepository;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import com.keypoint.keypointtravel.notification.service.PushNotificationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final ReadMemberService readMemberService;
    private final FCMTokenRepository fcmTokenRepository;
    private final PushNotificationHistoryService pushNotificationHistoryService;
    private final PushNotificationService pushNotificationService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendFCMNotification(PushNotificationEvent event) {
        PushNotificationType type = event.getPushNotificationType();

        for (Long memberId : event.getMemberIds()) {
            Member member = readMemberService.findMemberById(memberId);
            LanguageCode languageCode = member.getMemberDetail().getLanguage();

            // 1. FCM 내용 구성
            PushNotificationDTO notificationContent = pushNotificationService.generateNotification(
                event,
                languageCode);
            if (notificationContent == null) {
                return;
            }
            Notification notification = Notification.builder()
                .setTitle(notificationContent.getTitle())
                .setBody(notificationContent.getBody())
                .build();

            // 2. FCM Message 생성
            List<Message> messages = new ArrayList<>();
            Map<Integer, String> tokenMapper = new HashMap<>(); // Message hashcode&토큰 매퍼 생성 (Message에서 token을 get할 수 없음)
            List<String> tokens = fcmTokenRepository.findTokenByMemberId(memberId);
            for (String token : tokens) {
                Message message = Message
                    .builder()
                    .setNotification(notification)
                    .setToken(token)
                    .build();
                tokenMapper.put(message.hashCode(), token);
                messages.add(
                    message
                );
            }

            // 3. FCM 전송
            List<Integer> failedHashcodes = FCMUtils.sendMultiMessage(messages);

            // 4. 실패한 데이터 존재 시, 토큰 삭제
            pushNotificationService.deleteFailedToken(failedHashcodes, tokenMapper);

            // 5. 이력 저장
            PushNotificationHistory history = PushNotificationHistory.of(
                notificationContent.getTitle(),
                notificationContent.getBody(),
                type,
                member);
            pushNotificationHistoryService.savePushNotificationHistory(history);

        }
    }
}
