package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

public interface PushNotificationHistoryCustomRepository {

    boolean existsByIsReadFalseAndMemberId(Long memberId);
}
