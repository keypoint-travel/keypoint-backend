package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.notification.dto.response.PushHistoryResponse;
import com.keypoint.keypointtravel.notification.dto.useCase.ReadPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.repository.pushNotificationHistory.PushNotificationHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationHistoryService {

    private final PushNotificationHistoryRepository pushNotificationHistoryRepository;


    @Transactional
    public void savePushNotificationHistories(List<PushNotificationHistory> histories) {
        pushNotificationHistoryRepository.saveAll(histories);
    }

    /**
     * 읽지 않은 알림이 존재하는지 확인하는 함수
     *
     * @param useCase
     */
    public IsExistedResponse checkIsExistedUnreadPushNotification(MemberIdUseCase useCase) {
        try {
            boolean isExisted = pushNotificationHistoryRepository.existsByIsReadFalseAndMemberId(
                useCase.getMemberId());

            return IsExistedResponse.from(isExisted);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 푸시 이력 조회 함수 - 조회한 함수는 isRead:true 로 변경
     *
     * @param useCase
     */
    public Slice<PushHistoryResponse> findPushHistories(ReadPushHistoryUseCase useCase) {
        try {
            return pushNotificationHistoryRepository.findPushHistories(
                useCase.getMemberId(),
                useCase.getPageable()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
