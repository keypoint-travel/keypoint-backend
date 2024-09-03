package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.campaign.repository.CampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadReceiptService {

    private final CampaignRepository campaignRepository;
    private final ReceiptRepository receiptRepository;

    /**
     * 영수증 데이터 유효성 검사
     *
     * @param registrationType
     * @param addressAddress
     * @param longitude
     * @param latitude
     */
    public void validateReceipt(
        Long campaignId,
        String receiptId,
        String receiptImageUrl,
        ReceiptRegistrationType registrationType,
        String addressAddress,
        Double longitude,
        Double latitude
    ) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }

        if (registrationType == ReceiptRegistrationType.PHOTO) {
            if (addressAddress == null || longitude == null || latitude == null) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "영수증 주소 혹은 경위도가 null일 수 없습니다.");
            }
            if (receiptImageUrl == null) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "영수증 이미지가 null일 수 없습니다.");
            } else if (receiptId == null || receiptImageUrl.isBlank()) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "영수증 id가 null일 수 없습니다.");
            }
        } else {
            if (addressAddress != null &&
                (longitude == null || latitude == null)) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "영수증 주소가 압력된 상태일 때는 경위도가 null일 수 없습니다.");
            }
        }
    }
}
