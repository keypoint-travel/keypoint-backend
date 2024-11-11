package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.dto.useCase.SearchPageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import com.keypoint.keypointtravel.notification.dto.dto.PushNotificationDTO;
import com.keypoint.keypointtravel.notification.dto.response.AppPushHistoryResponse;
import com.keypoint.keypointtravel.notification.dto.response.WebPushHistoryResponse;
import com.keypoint.keypointtravel.notification.dto.useCase.CommonPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.PushHistoryIdUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.ReadPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.event.pushNotification.AdminPushNotificationEvent;
import com.keypoint.keypointtravel.notification.repository.pushNotificationHistory.PushNotificationHistoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final MemberRepository memberRepository;
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
     * [앱] 푸시 이력 조회 함수
     *
     * @param useCase
     */
    public Page<AppPushHistoryResponse> findPushHistories(ReadPushHistoryUseCase useCase) {
        try {
            Pageable pageable = useCase.getPageable();
            String memberName = memberRepository.findNameByMemberId(useCase.getMemberId());
            LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId()
            );

            // 1. 푸시 데이터 조회
            List<CommonPushHistoryUseCase> histories = pushNotificationHistoryRepository.findPushHistories(
                useCase.getMemberId(),
                pageable
            );
            long count = pushNotificationHistoryRepository.countPushHistories(
                useCase.getMemberId()
            );

            // 3. 다국어 적용 및 response 적용 변환
            List<AppPushHistoryResponse> translatedHistories = new ArrayList<>();
            for (CommonPushHistoryUseCase history : histories) {

                PushNotificationDTO notificationDTO = pushNotificationService.generateNotificationDTO(
                    memberName,
                    languageCode,
                    history.getDetailData(),
                    history.getType()
                );
                translatedHistories.add(
                    AppPushHistoryResponse.of(
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

    /**
     * [관리자] 푸시 이력 조회 함수
     *
     * @param useCase
     */
    public Page<WebPushHistoryResponse> findPushNotificationHistoryInWeb(
        SearchPageAndMemberIdUseCase useCase
    ) {
        try {
            Pageable pageable = useCase.getPageable();
            LanguageCode languageCode = LanguageCode.EN;

            // 1. 푸시 데이터 조회
            List<CommonPushHistoryUseCase> histories = pushNotificationHistoryRepository.findPushHistoriesInWeb(
                useCase
            );
            long count = pushNotificationHistoryRepository.countPushHistoriesInWeb(
                useCase.getKeyword());

            // 3. 다국어 적용 및 response 적용 변환
            List<WebPushHistoryResponse> translatedHistories = new ArrayList<>();
            Map<Long, String> memberIdAndNameMap = new HashMap<>(); // memberId : memberName
            for (CommonPushHistoryUseCase history : histories) {
                String memberName = getMemberName(history.getMemberId(), memberIdAndNameMap);
                PushNotificationDTO notificationDTO = pushNotificationService.generateNotificationDTO(
                    memberName,
                    languageCode,
                    history.getDetailData(),
                    history.getType()
                );
                translatedHistories.add(
                    WebPushHistoryResponse.of(
                        history.getHistoryId(),
                        notificationDTO.getTitle(),
                        memberName,
                        history.getMemberId(),
                        history.getArrivedAt()
                    )
                );
            }

            return new PageImpl<>(translatedHistories, pageable, count);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    private String getMemberName(Long memberId, Map<Long, String> memberIdAndNameMap) {
        if (memberIdAndNameMap.containsKey(memberId)) { // 존재하는 경우, map 데이터 반환
            return memberIdAndNameMap.get(memberId);
        } else { // 존재하지 않는 경우, 새로 데이터 조회
            String memberName = memberRepository.findNameByMemberId(memberId);
            memberIdAndNameMap.put(memberId, memberName);

            return memberName;
        }
    }
}
