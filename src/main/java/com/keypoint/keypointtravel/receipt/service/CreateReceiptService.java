package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.ImageUtils;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptIdUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
import com.keypoint.keypointtravel.receipt.redis.service.TempReceiptService;
import com.keypoint.keypointtravel.receipt.repository.ReceiptRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.awt.image.BufferedImage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReadReceiptService readReceiptService;
    private final CampaignRepository campaignRepository;
    private final MemberCampaignRepository memberCampaignRepository;
    private final UploadFileService uploadFileService;
    private final CampaignBudgetRepository campaignBudgetRepository;
    private final PaymentItemService paymentItemService;
    private final TempReceiptService tempReceiptService;

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
            readReceiptService.validateReceipt(
                campaignId,
                useCase.getReceiptId(),
                useCase.getReceiptImageUrl(),
                useCase.getRegistrationType(),
                useCase.getStoreAddress(),
                useCase.getLongitude(),
                useCase.getLatitude()
            );

            // 2. 영수증 저장
            // 2-1. 영수증 이미지 저장
            Campaign campaign = campaignRepository.getReferenceById(campaignId);
            BufferedImage image = ImageUtils.convertImageUrlToImage(receiptImageUrl);
            Long receiptImageId = uploadFileService.saveUploadFile(receiptImageUrl, image);
            CurrencyType currencyType = campaignBudgetRepository.findCurrencyByCampaignId(
                campaignId
            );
        // 2-2. 임시 영수증 데이터 가져오기
        TempReceipt tempReceipt = tempReceiptService.findTempReceiptById(
            useCase.getReceiptId());
        Receipt receipt = useCase.toEntity(campaign, receiptImageId, currencyType, tempReceipt);
            receiptRepository.save(receipt);

            // 3. 결제 항목 저장
            // 3-1. 참여 리스트가 모두 캠페인에 초대된 회원인지 확인
            List<Member> invitedMembers = memberCampaignRepository.findMembersByCampaignId(
                campaignId
            );
            List<Long> invitedMemberIds = invitedMembers.stream().map(Member::getId).toList();
            for (CreatePaymentItemUseCase paymentItem : useCase.getPaymentItems()) {
                // 3-1. 참여 리스트가 모두 캠페인에 초대된 회원인지 확인
                if (!invitedMemberIds.containsAll(paymentItem.getMemberIds())) {
                    throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                        "캠페인에 초대되지 않은 참가자가 포함되어 있습니다.");
                }

                // 3-2 저장
                List<Member> filteredMembers = invitedMembers.stream()
                    .filter(member -> paymentItem.getMemberIds().contains(member.getId()))
                    .toList();
                paymentItemService.addPaymentItem(receipt, paymentItem, filteredMembers);
            }
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
}
