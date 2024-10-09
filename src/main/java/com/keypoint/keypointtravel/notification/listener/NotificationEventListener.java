package com.keypoint.keypointtravel.notification.listener;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.notification.MarketingNotificationType;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.global.utils.FCMUtils;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.dto.response.fcmBody.FCMBodyResponse;
import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.event.marketingNotification.MarketingNotificationEvent;
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

    private final MemberRepository memberRepository;
    private final FCMTokenRepository fcmTokenRepository;
    private final PushNotificationHistoryService pushNotificationHistoryService;
    private final PushNotificationService pushNotificationService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendFCMNotification(PushNotificationEvent event) {
        List<PushNotificationHistory> pushNotificationHistories = new ArrayList<>();
        List<AlarmMemberUserCase> useCases = memberRepository.findAlarmMembersByMemberIds(
            event.getMemberIds());

        for (AlarmMemberUserCase useCase : useCases) {
            Long memberId = useCase.getMemberId();
            PushNotificationType type = event.getPushNotificationType();
            // 알림 설정이 안되어 있는 경우
            if (!useCase.isPushNotificationEnabled()) {
                continue;
            }

            // 1. FCM 내용 구성
            PushNotificationContent notificationMsg = PushNotificationContent.getRandomNotificationContent(
                type
            );
            PushNotificationDTO notificationContent = pushNotificationService.generateNotificationDTO(
                useCase.getName(),
                useCase.getLanguage(),
                event,
                notificationMsg
            );
            if (notificationContent == null) {
                continue;
            }

            // 2. 토큰이 존재할 때만 FCM 전송
            List<String> tokens = fcmTokenRepository.findTokenByMemberId(memberId);
            if (!tokens.isEmpty()) {
                // 2-1. FCM Message 생성
                Map<Integer, String> tokenMapper = new HashMap<>(); // Message hashcode&토큰 매퍼 생성 (Message에서 token을 get할 수 없음)
                List<Message> messages = generateMessages(memberId, notificationContent, type,
                    tokenMapper, tokens);

                // 2-2. FCM 전송
                List<Integer> failedHashcodes = FCMUtils.sendMultiMessage(messages);

                // 2-3. 실패한 데이터 존재 시, 토큰 삭제
                pushNotificationService.deleteFailedToken(failedHashcodes, tokenMapper);
            }

            // 3. 이력 객체 생성
            pushNotificationHistories.add(PushNotificationHistory.of(
                notificationContent.getPushNotificationContent(),
                memberRepository.getReferenceById(memberId),
                event
            ));
        }

        // 4. 이력 저장
        event.clearMemberIds();
        pushNotificationHistoryService.savePushNotificationHistories(pushNotificationHistories);
    }

    private List<Message> generateMessages(
        Long memberId,
        PushNotificationDTO notificationContent,
        PushNotificationType type,
        Map<Integer, String> tokenMapper,
        List<String> tokens
    ) {
        Object detail = pushNotificationService.generateNotificationDetail(
            memberId,
            type
        );

        FCMBodyResponse body = FCMBodyResponse.of(type, notificationContent.getBody(), detail);
        Notification notification = Notification.builder()
            .setTitle(notificationContent.getTitle())
            .setBody(body.toString())
            .build();

        List<Message> messages = new ArrayList<>();

        for (String token : tokens) {
            Message message = Message
                .builder()
                .setNotification(notification)
                .setToken(token)
                .build();
            tokenMapper.put(message.hashCode(), token);
            messages.add(message);
        }

        return messages;
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMarketingNotification(MarketingNotificationEvent event) {
        MarketingNotificationType type = event.getMarketingNotificationType();
        EmailTemplate emailTemplate = type.getTemplate();

        EmailUtils.sendMultiEmail(
            event.getMemberEmails()
            , emailTemplate,
            null
        );
    }
}
