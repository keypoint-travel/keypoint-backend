package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.global.dto.useCase.SearchPageAndMemberIdUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.CommonPushHistoryUseCase;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PushNotificationHistoryCustomRepository {

    boolean existsByIsReadFalseAndMemberId(Long memberId);

    List<CommonPushHistoryUseCase> findPushHistories(Long memberId, Pageable pageable);

    long countPushHistories(Long memberId);

    List<CommonPushHistoryUseCase> findPushHistoriesInWeb(SearchPageAndMemberIdUseCase useCase);

    long countPushHistoriesInWeb(String keyword);
}
