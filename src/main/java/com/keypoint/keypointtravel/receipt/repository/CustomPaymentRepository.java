package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.ReceiptInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
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

    private final QPaymentItem qPaymentItem = QPaymentItem.paymentItem;

    private final QPaymentMember qPaymentMember = QPaymentMember.paymentMember;

    private final QReceipt qReceipt = QReceipt.receipt;

    private final QMember qMember = QMember.member;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    // campaignId 에 해당하는 영수중 중에 카테고리 별 그룹화하여 비용의 합을 계산 AmountByCategoryDto
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

    // campaignId 에 해당하는 영수중 중에 paid_at를 (yy.mm.dd)로 형식을 지정 후 그룹화하여 비용의 합을 계산 AmountByDateDto
    public List<AmountByDateDto> findAmountByDate(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(AmountByDateDto.class,
                    Expressions.stringTemplate("DATE_FORMAT({0}, {1})", qReceipt.paidAt.stringValue(), "%y.%m.%d"),
                    qReceipt.totalAmount.sum()))
            .from(qReceipt)
            .where(qReceipt.campaign.id.eq(campaignId))
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0}, {1})", qReceipt.paidAt.stringValue(), "%y.%m.%d"))
            .fetch();
    }

    public List<ReceiptInfoDto> findAllOrderByPaidAt(Long campaignId){
        return queryFactory.select(
            Projections.constructor(ReceiptInfoDto.class,
                qReceipt.id,
                uploadFile.path,
                qReceipt.paidAt,
                qReceipt.latitude,
                qReceipt.longitude))
            .from(qReceipt)
            .leftJoin(uploadFile).on(qReceipt.receiptImageId.eq(uploadFile.id))
            .where(qReceipt.campaign.id.eq(campaignId)
                .and(qReceipt.latitude.isNotNull())
                .and(qReceipt.longitude.isNotNull()))
            .orderBy(qReceipt.paidAt.asc())
            .fetch();
    }
}
