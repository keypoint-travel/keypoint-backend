package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignListUseCase;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignWaitMemberRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteCampaignService {

    private final static PushNotificationType PUSH_NOTIFICATION_TYPE = PushNotificationType.CAMPAIGN_END;

    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignRepository campaignRepository;

    private final CampaignWaitMemberRepository campaignWaitMemberRepository;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 캠페인 종료 함수
     *
     * @Param campaignId, memberId useCase
     */
    @Transactional
    public void completeCampaign(CompleteCampaignUseCase useCase) {
        // 1. 캠페인 장인지 확인(알림 발송을 위해 회원 리스트 조회)
        List<Long> memberIds = validateIsLeader(useCase.getMemberId(), useCase.getCampaignId());
        // 2. 캠페인 종료 (status 변경)
        campaignRepository.updateCampaignFinished(useCase.getCampaignId());
        // 3. 캠페인 대기 목록 삭제
        campaignWaitMemberRepository.deleteAllByCampaignId(useCase.getCampaignId());
        // 4. 캠페인 종료 알림 전송, 배지 부여
        List<Long> invitedMemberIds = memberCampaignRepository.findMemberIdsByCampaignId(
            useCase.getCampaignId()
        );
        eventPublisher.publishEvent(CampaignPushNotificationEvent.of(
                PUSH_NOTIFICATION_TYPE,
                invitedMemberIds,
                useCase.getCampaignId()
            )
        );

    }

    private List<Long> validateIsLeader(Long memberId, Long campaignId) {
        List<MemberCampaign> memberCampaigns = memberCampaignRepository.findAllByCampaignId(campaignId);
        // 요청한 member가 캠페인 장인지 확인, getId() 호출이기에 n + 1 문제 발생 가능성 없음.
        for (MemberCampaign memberCampaign : memberCampaigns) {
            if (memberCampaign.getMember().getId().equals(memberId)) {
                if (!memberCampaign.isLeader()) {
                    throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
                }
                break;
            }
        }
        List<Long> memberIds = memberCampaigns.stream()
            .map(memberCampaign -> memberCampaign.getMember().getId())
            .toList();
        // 회원-캠페인에 memberId가 존재하지 않을 경우
        if (!memberIds.contains(memberId)) {
            throw new GeneralException(CampaignErrorCode.NOT_CAMPAIGN_OWNER);
        }
        return memberIds;
    }

    /**
     * 종료된 캠페인 목록 조회 함수
     *
     * @Param memberId useCase
     * @Return List<CampaignInfoDto>
     */
    @Transactional(readOnly = true)
    public Page<CampaignResponse> findCampaigns(FIndCampaignListUseCase useCase){
        // 1. 캠페인 정보 조회
        CampaignDto dto =  campaignRepository.findCampaignInfoList(
            useCase.getMemberId(), Status.FINISHED, useCase.getSize(), useCase.getPage());
        // 2. 페이지 형태로 반환
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(
            useCase.getPage() > 0 ? useCase.getPage() - 1 : 0, useCase.getSize() > 0 ? useCase.getSize() : 1, sort);
        return new PageImpl<>(CampaignResponse.from(dto.getDtoList()), pageable, dto.getTotalCount());
    }
}
