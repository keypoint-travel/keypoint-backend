package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.dto.response.PushHistoryResponse;
import com.keypoint.keypointtravel.notification.dto.useCase.CommonPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.PushHistoryIdUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.ReadPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.event.pushNotification.AdminPushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.pushNotificationHistory.PushNotificationHistoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationHistoryService {

    private final PushNotificationHistoryRepository pushNotificationHistoryRepository;
    private final PushNotificationService pushNotificationService;
    private final MemberDetailRepository memberDetailRepository;
    private final ReadMemberService readMemberService;


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
    public Page<PushHistoryResponse> findPushHistories(ReadPushHistoryUseCase useCase) {
        try {
            Pageable pageable = useCase.getPageable();
            MemberDetail memberDetail = memberDetailRepository.findByMemberId(
                useCase.getMemberId());

            // 1. 푸시 데이터 조회
            List<CommonPushHistoryUseCase> histories = pushNotificationHistoryRepository.findPushHistories(
                useCase.getMemberId(),
                pageable
            );
            long count = pushNotificationHistoryRepository.countPushHistories(
                useCase.getMemberId()
            );

            // 3. 다국어 적용 및 response 적용 변환
            List<PushHistoryResponse> translatedHistories = new ArrayList<>();
            for (CommonPushHistoryUseCase history : histories) {

                PushNotificationDTO notificationDTO = pushNotificationService.generateNotificationDTO(
                    memberDetail,
                    history.getDetailData(),
                    history.getType()
                );
                translatedHistories.add(
                    PushHistoryResponse.of(
                        history.getHistoryId(),
                        notificationDTO.getTitle(),
                        notificationDTO.getBody(),
                        history.getArrivedAt(),
                        history.isRead()
                    )
                );
            }

            return new PageImpl<>(translatedHistories, pageable, count);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 알림 이력을 읽은 상태로 변경
     *
     * @param useCase
     */
    public void markPushHistoryAsRead(PushHistoryIdUseCase useCase) {
        try {
            pushNotificationHistoryRepository.updateIsReadTrueByHistoryId(
                useCase.getHistoryId(),
                useCase.getMemberId()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    @Transactional
    public void savePushNotificationHistories(CreatePushNotificationUseCase useCase) {
        try {
            Member member = readMemberService.findMemberById(useCase.getMemberId());

            AdminPushNotificationEvent event = new AdminPushNotificationEvent(
                    PushNotificationType.PUSH_NOTIFICATION_BY_ADMIN,
                    useCase.getTitle(),
                    useCase.getBody()
            );

            PushNotificationHistory history = PushNotificationHistory.of(
                    PushNotificationContent.PUSH_NOTIFICATION_BY_ADMIN,
                    member,
                    event
            );

            pushNotificationHistoryRepository.save(history);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
