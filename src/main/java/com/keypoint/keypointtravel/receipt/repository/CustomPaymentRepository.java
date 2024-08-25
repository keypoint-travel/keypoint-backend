package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.ReceiptInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.PaymentByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.receipt.entity.QPaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentMember;
import com.keypoint.keypointtravel.receipt.entity.QReceipt;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPaymentRepository {

    private final JPAQueryFactory queryFactory;

    private final QPaymentItem paymentItem = QPaymentItem.paymentItem;

    private final QPaymentMember paymentMember = QPaymentMember.paymentMember;

    private final QReceipt receipt = QReceipt.receipt;

    private final QMember member = QMember.member;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    // campaignId 에 해당하는 영수중 중에 카테고리 별 그룹화하여 비용의 합을 계산 AmountByCategoryDto
    public List<AmountByCategoryDto> findAmountByCategory(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(AmountByCategoryDto.class,
                    receipt.receiptCategory,
                    receipt.totalAmount.sum()))
            .from(receipt)
            .where(receipt.campaign.id.eq(campaignId))
            .groupBy(receipt.receiptCategory)
            .fetch();
    }

    // campaignId 에 해당하는 영수중 중에 paid_at를 (yy.mm.dd)로 형식을 지정 후 그룹화하여 비용의 합을 계산 AmountByDateDto
    public List<AmountByDateDto> findAmountByDate(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(AmountByDateDto.class,
                    Expressions.stringTemplate("DATE_FORMAT({0}, {1})", receipt.paidAt.stringValue(), "%y.%m.%d"),
                    receipt.totalAmount.sum()))
            .from(receipt)
            .where(receipt.campaign.id.eq(campaignId))
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0}, {1})", receipt.paidAt.stringValue(), "%y.%m.%d"))
            .fetch();
    }

    public List<ReceiptInfoDto> findAllOrderByPaidAt(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(ReceiptInfoDto.class,
                    receipt.id,
                    uploadFile.path,
                    receipt.paidAt,
                    receipt.latitude,
                    receipt.longitude))
            .from(receipt)
            .leftJoin(uploadFile).on(receipt.receiptImageId.eq(uploadFile.id))
            .where(receipt.campaign.id.eq(campaignId)
                .and(receipt.latitude.isNotNull())
                .and(receipt.longitude.isNotNull()))
            .orderBy(receipt.paidAt.asc())
            .fetch();
    }

    // campaignId와 category에 해당하는 page, size에 맞는 결제 항목 조회
    public List<PaymentByCategoryDto> findPaymentsByCategory(Long campaignId, ReceiptCategory category, int size, int page) {
        return queryFactory.select(
                Projections.constructor(PaymentByCategoryDto.class,
                    paymentItem.id,
                    receipt.store,
                    receipt.paidAt,
                    paymentItem.amount,
                    paymentItem.quantity,
                    receipt.currency,
                    receipt.id))
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId)
                .and(receipt.receiptCategory.eq(category)))
            .orderBy(receipt.paidAt.desc())
            .offset((long) size * (page > 0 ? page - 1 : 0))
            .limit(size)
            .fetch();
    }

    // campaignId와 category에 해당하는 결제 항목의 총 합 조회
    public Long countPaymentByCategory(Long campaignId, ReceiptCategory category) {
        return queryFactory.select(paymentItem.count())
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId)
                .and(receipt.receiptCategory.eq(category)))
            .fetchOne();
    }

    // campaignId와 category에 해당하는 회원 리스트 조회
    public List<PaymentMemberDto> findMembersByCampaignIdAndCategory(Long campaignId, ReceiptCategory category) {
        return queryFactory.select(
                Projections.constructor(PaymentMemberDto.class,
                    paymentItem.id,
                    member.id,
                    member.memberDetail.name))
            .from(paymentMember)
            .innerJoin(paymentMember.member, member)
            .innerJoin(paymentMember.paymentItem, paymentItem)
            .where(paymentItem.receipt.campaign.id.eq(campaignId)
                .and(paymentItem.receipt.receiptCategory.eq(category)))
            .fetch();
    }
}
