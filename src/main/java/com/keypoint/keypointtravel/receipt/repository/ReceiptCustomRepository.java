package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.response.CampaignReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase.UpdateReceiptUseCase;
import java.util.List;


public interface ReceiptCustomRepository {

    ReceiptResponse findReceiptDetailByReceiptId(Long receiptId);

    void deleteReceiptById(Long receiptId);

    void updateReceipt(UpdateReceiptUseCase useCase);

    List<CampaignReceiptResponse> findReceiptsByCampaign(Long campaignId);
}
