package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceipt.UpdatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentItemCustomRepositoryImpl implements PaymentItemCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QPaymentItem paymentItem = QPaymentItem.paymentItem;
    private final QPaymentMember paymentMember = QPaymentMember.paymentMember;

    @Override
    public long updatePaymentItem(UpdatePaymentItemUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        return queryFactory
            .update(paymentItem)
            .set(paymentItem.itemName, useCase.getItemName())
            .set(paymentItem.amount, useCase.getAmount())
            .set(paymentItem.quantity, useCase.getQuantity())

            .set(paymentItem.modifyId, currentAuditor)
            .set(paymentItem.modifyAt, LocalDateTime.now())
            .where(paymentItem.id.eq(useCase.getPaymentItemId()))
            .execute();
    }

    @Override
    public void deleteAllByIds(List<PaymentItem> itemsToDelete) {
        // PaymentMember 삭제
        queryFactory
            .delete(paymentMember)
            .where(paymentMember.paymentItem.in(itemsToDelete))
            .execute();

        // PaymentItem 삭제
        queryFactory
            .delete(paymentItem)
            .where(paymentItem.in(itemsToDelete))
            .execute();
    }
}
