package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceipt.UpdatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import java.util.List;


public interface PaymentItemCustomRepository {

    long updatePaymentItem(UpdatePaymentItemUseCase paymentItem);

    void deleteAllByIds(List<PaymentItem> itemsToDelete);
}
