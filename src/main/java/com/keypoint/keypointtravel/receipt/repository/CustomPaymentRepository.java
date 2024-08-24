package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.receipt.entity.QPaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentMember;
import com.keypoint.keypointtravel.receipt.entity.QReceipt;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPaymentRepository {

    private final JPAQueryFactory queryFactory;

    private final QPaymentItem qPaymentItem = QPaymentItem.paymentItem;

    private final QPaymentMember qPaymentMember = QPaymentMember.paymentMember;

    private final QReceipt qReceipt = QReceipt.receipt;

    private final QMember qMember = QMember.member;

    public List<PaymentDto> findPaymentList(Long campaignId) {
        // 캠페인 아이디를 통해 결제 항목 리스트 조회
        return queryFactory.select(
                Projections.constructor(PaymentDto.class,
                    qReceipt.id,
                    qReceipt.receiptCategory,
                    qReceipt.paidAt,
                    qPaymentItem.id,
                    qReceipt.store,
                    qPaymentItem.quantity,
                    qPaymentItem.amount,
                    qReceipt.currency))
            .from(qPaymentItem)
            .innerJoin(qPaymentItem.receipt, qReceipt)
            .where(qReceipt.campaign.id.eq(campaignId))
            .orderBy(qReceipt.paidAt.desc())
            .fetch();
    }

    public List<PaymentMemberDto> findMemberListByPayments(List<Long> paymentItemIds) {
        // 결제 항목 별 참여 인원 리스트 조회
        return queryFactory.select(
                Projections.constructor(PaymentMemberDto.class,
                    qPaymentMember.paymentItem.id,
                    qPaymentMember.member.id,
                    qMember.memberDetail.name))
            .from(qPaymentMember)
            .innerJoin(qPaymentMember.member, qMember)
            .where(qPaymentMember.paymentItem.id.in(paymentItemIds))
            .fetch();
    }

    // campaignId 에 해당하는 영수중 중에 카테고리 별 그룹 화하여 비용의 합을 계산 AmountByCategoryDto
    public List<AmountByCategoryDto> findAmountByCategory(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(AmountByCategoryDto.class,
                    qReceipt.receiptCategory,
                    qReceipt.totalAmount.sum()))
            .from(qReceipt)
            .where(qReceipt.campaign.id.eq(campaignId))
            .groupBy(qReceipt.receiptCategory)
            .fetch();
    }
}
