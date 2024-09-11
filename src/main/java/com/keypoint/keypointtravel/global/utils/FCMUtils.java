package com.keypoint.keypointtravel.global.utils;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;
import com.keypoint.keypointtravel.global.enumType.error.FCMErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FCMUtils {

    private static FirebaseMessaging firebaseMessaging;
    private static int FCM_MAX_MESSAGE_COUNT = 500;

    /**
     * 단일 푸시 메세지를 보내는 함수
     *
     * @param message 보낼 데이터
     */
    public static void sendSingleMessage(Message message) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new GeneralException(FCMErrorCode.FAIL_TO_SEND_FCM, e);
        }
    }

    /**
     * 여러 개의 푸시 메세지를 보내는 함수
     *
     * @param messages 보낼 데이터
     * @return 실패한 메세지의 hashcode
     */
    public static List<Integer> sendMultiMessage(List<Message> messages) {
        try {
            // 메시지를 FCM_MAX_MESSAGE_COUNT 개씩 나누기
            List<List<Message>> batches = partitionMessages(messages, FCM_MAX_MESSAGE_COUNT);

            // 각 배치에 대해 sendAll 호출
            List<Integer> failedHashCode = new ArrayList<>();
            for (List<Message> batch : batches) {
                BatchResponse response = FirebaseMessaging.getInstance().sendEach(batch);
                failedHashCode.addAll(findFailedMessageHashCode(batch, response.getResponses()));
            }

            return failedHashCode;
        } catch (FirebaseMessagingException e) {
            throw new GeneralException(FCMErrorCode.FAIL_TO_SEND_FCM, e);
        }
    }

    /**
     * 메시지 리스트를 지정된 크기로 나누는 함수
     *
     * @param messages  메시지 리스트
     * @param batchSize 배치 크기
     * @return 나누어진 배치 리스트
     */
    private static List<List<Message>> partitionMessages(List<Message> messages, int batchSize) {
        List<List<Message>> batches = new ArrayList<>();
        int totalMessages = messages.size();

        for (int i = 0; i < totalMessages; i += batchSize) {
            int end = Math.min(totalMessages, i + batchSize);
            batches.add(new ArrayList<>(messages.subList(i, end)));
        }

        return batches;
    }


    /**
     * 응답 중 전송에 실패한 fcm 토큰을 찾는 함수
     *
     * @param messages  전송 정보
     * @param responses 응답 정보
     * @return
     */
    private static List<Integer> findFailedMessageHashCode(List<Message> messages,
        List<SendResponse> responses) {
        List<Integer> failedData = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            SendResponse response = responses.get(i);
            if (!response.isSuccessful()) {
                failedData.add(messages.get(i).hashCode());
            }
        }

        return failedData;
    }

    @Autowired
    public void setFirebaseMessaging(FirebaseMessaging firebaseMessaging) {
        FCMUtils.firebaseMessaging = firebaseMessaging;
    }
}
