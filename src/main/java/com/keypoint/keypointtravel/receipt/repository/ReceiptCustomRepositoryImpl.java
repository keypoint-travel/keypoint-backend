package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.PaymentItemResponse;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.entity.QPaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentMember;
import com.keypoint.keypointtravel.receipt.entity.QReceipt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReceiptCustomRepositoryImpl implements ReceiptCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QReceipt receipt = QReceipt.receipt;
    private final QPaymentItem paymentItem = QPaymentItem.paymentItem;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;
    private final QPaymentMember paymentMember = QPaymentMember.paymentMember;
    private final QMember member = QMember.member;

    @Override
    public ReceiptResponse findReceiptDetailByReceiptId(Long receiptId) {
        BooleanBuilder receiptBuilder = new BooleanBuilder();
        receiptBuilder.and(receipt.id.eq(receiptId))
            .and(receipt.isDeleted.eq(false));

        List<ReceiptResponse> responses = queryFactory
            .selectFrom(receipt)
            .leftJoin(uploadFile).on(uploadFile.id.eq(receipt.receiptImageId))
            .where(receiptBuilder)
            .transform(
                GroupBy.groupBy(receipt.id).list(
                    Projections.constructor(
                        ReceiptResponse.class,
                        receipt.paidAt,
                        receipt.id,
                        receipt.store,
                        receipt.storeAddress,
                        receipt.receiptCategory,
                        uploadFile.path,
                        receipt.totalAmount,
                        receipt.currency,
                        receipt.memo
                    )
                )
            );

        if (responses == null) {
            throw new GeneralException(ReceiptError.NOT_EXISTED_RECEIPT);
        }

        ReceiptResponse response = responses.get(0);
        List<PaymentItemResponse> paymentItemResponses
            = queryFactory
            .selectFrom(paymentItem)
            .where(paymentItem.receipt.id.eq(receiptId))
            .leftJoin(paymentMember).on(paymentMember.paymentItem.eq(paymentItem))
            .transform(
                GroupBy.groupBy(paymentItem.id).list(
                    Projections.fields(
                        PaymentItemResponse.class,
                        paymentItem.id.as("paymentItemId"),
                        paymentItem.itemName,
                        paymentItem.quantity,
                        paymentItem.amount,
                        GroupBy.list(paymentMember.member.id).as("memberIds")
                    )
                )
            );
        response.setPaymentItems(paymentItemResponses);

        return response;
    }
}
