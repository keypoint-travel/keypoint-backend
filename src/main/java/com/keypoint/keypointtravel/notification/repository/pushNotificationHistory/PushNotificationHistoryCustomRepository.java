package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.notification.dto.response.PushHistoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PushNotificationHistoryCustomRepository {

    boolean existsByIsReadFalseAndMemberId(Long memberId);

    Slice<PushHistoryResponse> findPushHistories(Long memberId, Pageable pageable);
}
