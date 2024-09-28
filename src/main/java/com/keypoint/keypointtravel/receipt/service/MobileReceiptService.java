package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.ImageUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import com.keypoint.keypointtravel.receipt.dto.response.CampaignReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.CampaignIdUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptIdUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceipt.CreatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceipt.CreateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceipt.UpdateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
import com.keypoint.keypointtravel.receipt.redis.service.TempReceiptService;
import com.keypoint.keypointtravel.receipt.repository.ReceiptRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MobileReceiptService {

    private final ReceiptRepository receiptRepository;
    private final CampaignRepository campaignRepository;
    private final MemberCampaignRepository memberCampaignRepository;
    private final UploadFileService uploadFileService;
    private final CampaignBudgetRepository campaignBudgetRepository;
    private final PaymentItemService paymentItemService;
    private final TempReceiptService tempReceiptService;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * (영수증 생성) 영수증 데이터 유효성 검사
     *
     * @param campaignId
     * @param receiptId
     * @param receiptImageUrl
     * @param registrationType
     */
    public void validateReceiptInCreate(
            Long campaignId,
            String receiptId,
            String receiptImageUrl,
        ReceiptRegistrationType registrationType
    ) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }

        if (registrationType == ReceiptRegistrationType.PHOTO) {
            if (receiptImageUrl == null) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "영수증 이미지가 null일 수 없습니다.");
            } else if (receiptId == null || receiptId.isBlank()) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "영수증 id가 null일 수 없습니다.");
            }
        }
    }

    /**
     * (영수증 수정) 영수증 데이터 유효성 검사
     *
     * @param registrationType
     * @param addressAddress
     * @param longitude
     * @param latitude
     */
    public void validateReceiptInUpdate(
            ReceiptRegistrationType registrationType,
            String addressAddress,
            Double longitude,
            Double latitude
    ) {
        if (registrationType == ReceiptRegistrationType.PHOTO) {
            if (addressAddress == null || longitude == null || latitude == null) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "영수증 주소 혹은 경위도가 null일 수 없습니다.");
            }
        } else {
            if (addressAddress != null &&
                    (longitude == null || latitude == null)) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "영수증 주소가 압력된 상태일 때는 경위도가 null일 수 없습니다.");
            }
        }
    }

    /**
     * 영수증 등록 함수
     *
     * @param useCase
     */
    @Transactional
    public void addReceipt(CreateReceiptUseCase useCase) {
        try {
            Long campaignId = useCase.getCampaignId();
            String receiptImageUrl = useCase.getReceiptImageUrl();

            // 1. 유효성 확인 (캠페인 아이디, 주소, 경도, 위도)
            validateReceiptInCreate(
                campaignId,
                useCase.getReceiptId(),
                useCase.getReceiptImageUrl(),
                useCase.getRegistrationType()
            );

            // 2. 영수증 저장
            Campaign campaign = campaignRepository.getReferenceById(campaignId);
            CurrencyType currencyType = campaignBudgetRepository.findCurrencyByCampaignId(
                campaignId
            ).orElse(null);
            Receipt receipt;

            if (useCase.getRegistrationType() == ReceiptRegistrationType.PHOTO) {
                // 2-1. 영수증 이미지 저장
                BufferedImage image = ImageUtils.convertImageUrlToImage(receiptImageUrl);
                Long receiptImageId = uploadFileService.saveUploadFile(receiptImageUrl, image);

                // 2-2. 임시 영수증 데이터 가져오기
                TempReceipt tempReceipt = tempReceiptService.findTempReceiptById(
                    useCase.getReceiptId());
                receipt = useCase.toEntity(campaign, receiptImageId, currencyType,
                    tempReceipt);
            } else {
                receipt = useCase.toEntity(campaign, currencyType);
            }

            receiptRepository.save(receipt);

            // 3. 결제 항목 저장
            // 3-1. 참여 리스트가 모두 캠페인에 초대된 회원인지 확인
            List<Member> invitedMembers = memberCampaignRepository.findMembersByCampaignId(
                campaignId
            );
            List<Long> invitedMemberIds = invitedMembers.stream().map(Member::getId).toList();
            Set<Long> receiptMemberIds = new HashSet<>();
            for (CreatePaymentItemUseCase paymentItem : useCase.getPaymentItems()) {
                // 3-1. 참여 리스트가 모두 캠페인에 초대된 회원인지 확인
                receiptMemberIds.addAll(paymentItem.getMemberIds());
                if (!invitedMemberIds.containsAll(paymentItem.getMemberIds())) {
                    throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "캠페인에 초대되지 않은 참가자가 포함되어 있습니다.");
                }

                // 3-2 저장
                List<Member> filteredMembers = invitedMembers.stream()
                    .filter(member -> paymentItem.getMemberIds().contains(member.getId()))
                    .toList();
                paymentItemService.addPaymentItem(paymentItem.toEntity(receipt), filteredMembers);
            }

            // 4. 영수증 FCM 전달
            eventPublisher.publishEvent(CommonPushNotificationEvent.of(
                PushNotificationType.RECEIPT_REGISTER,
                receiptMemberIds.stream().toList()
            ));
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * @param useCase
     * @return
     */
    public ReceiptResponse getReceipt(ReceiptIdUseCase useCase) {
        try {
            return receiptRepository.findReceiptDetailByReceiptId(
                useCase.getReceiptId()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 영수증 삭제 함수
     *
     * @param useCase
     */
    @Transactional
    public void deleteReceipt(ReceiptIdUseCase useCase) {
        try {
            receiptRepository.deleteReceiptById(
                useCase.getReceiptId()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * @param useCase
     */
    @Transactional
    public void updateReceipt(UpdateReceiptUseCase useCase) {
        try {
            Optional<ReceiptRegistrationType> registrationType = receiptRepository.findReceiptRegistrationTypeByReceiptId(useCase.getReceiptId());
            if (registrationType.isEmpty()) {
                throw new GeneralException(ReceiptError.NOT_EXISTED_RECEIPT);
            }

            // 1. 유효성 확인 (주소, 경도, 위도)
            validateReceiptInUpdate(
                    registrationType.get(),
                    useCase.getStoreAddress(),
                    useCase.getLongitude(),
                    useCase.getLatitude()
            );

            // 영수증 수정
            receiptRepository.updateReceipt(useCase);

            // 결제 아이템 수정
            Receipt receipt = receiptRepository.getReferenceById(useCase.getReceiptId());
            paymentItemService.updatePaymentItem(receipt, useCase.getPaymentItems());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 캠페인에 등록된 영수증을 조회하는 함수
     *
     * @param useCase
     * @return
     */
    public List<CampaignReceiptResponse> getReceiptsAboutCampaign(CampaignIdUseCase useCase) {
        try {
            return receiptRepository.findReceiptsByCampaign(
                useCase.getCampaignId()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
