package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.ReceiptInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentInfo;
import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.receipt.entity.QPaymentItem;
import com.keypoint.keypointtravel.receipt.entity.QPaymentMember;
import com.keypoint.keypointtravel.receipt.entity.QReceipt;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import static com.querydsl.jpa.JPAExpressions.select;
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

    // campaignId에 해당하는 회원별 총 사용 금액
    public List<AmountByMemberDto> findAmountByMember(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(AmountByMemberDto.class,
                    paymentMember.member.id,
                    paymentMember.member.memberDetail.name,
                    uploadFile.path,
                    paymentItem.amount.multiply(paymentItem.quantity).divide(
                        select(paymentMember.count())
                            .from(paymentMember)
                            .where(paymentMember.paymentItem.id.eq(paymentItem.id))
                    )))
            .from(paymentItem)
            .innerJoin(paymentMember).on(paymentItem.id.eq(paymentMember.paymentItem.id))
            .leftJoin(uploadFile).on(paymentMember.member.memberDetail.profileImageId.eq(uploadFile.id))
            .where(paymentItem.receipt.campaign.id.eq(campaignId))
            .groupBy(paymentMember.member.id, paymentItem.id)
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
    public PaymentDto findPaymentsByCategory(Long campaignId, ReceiptCategory category, int size, int page) {
        List<PaymentInfo> payments = queryFactory.select(
                Projections.constructor(PaymentInfo.class,
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
            .offset((long) (size > 0 ? size : 1) * (page > 0 ? page - 1 : 0))
            .limit((size > 0 ? size : 1))
            .fetch();
        // campaignId와 category에 해당하는 결제 항목의 총 합 조회
        Long count = queryFactory.select(paymentItem.count())
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId)
                .and(receipt.receiptCategory.eq(category)))
            .fetchOne();
        return new PaymentDto(payments, count);
    }

    // campaignId와 date에 해당하는 page, size에 맞는 결제 항목 조회
    public PaymentDto findPaymentsByDate(Long campaignId, String date, int size, int page) {
        List<PaymentInfo> payments = queryFactory.select(
                Projections.constructor(PaymentInfo.class,
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
                .and(!date.equals("null") ? Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                    receipt.paidAt.stringValue(), "%y.%m.%d").eq(date) : Expressions.TRUE))
            .orderBy(receipt.paidAt.desc())
            .offset((long) (size > 0 ? size : 1) * (page > 0 ? page - 1 : 0))
            .limit((size > 0 ? size : 1))
            .fetch();
        // campaignId와 category에 해당하는 결제 항목의 총 합 조회
        Long count = queryFactory.select(paymentItem.count())
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId)
                .and(!date.equals("null") ? Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                    receipt.paidAt.stringValue(), "%y.%m.%d").eq(date) : Expressions.TRUE))
            .fetchOne();
        return new PaymentDto(payments, count);
    }

    public PaymentDto findPaymentsByPrice(Long campaignId, Direction direction, int size, int page) {
        OrderSpecifier<Float> orderSpecifier =
            new OrderSpecifier<>(direction.equals(Direction.ASC) ? Order.ASC
                : Order.DESC, paymentItem.amount.multiply(paymentItem.quantity));
        List<PaymentInfo> payments = queryFactory.select(
                Projections.constructor(PaymentInfo.class,
                    paymentItem.id,
                    receipt.store,
                    receipt.paidAt,
                    paymentItem.amount,
                    paymentItem.quantity,
                    receipt.currency,
                    receipt.id))
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId))
            .orderBy(orderSpecifier)
            .offset((long) (size > 0 ? size : 1) * (page > 0 ? page - 1 : 0))
            .limit((size > 0 ? size : 1))
            .fetch();
        // campaignId와 category에 해당하는 결제 항목의 총 합 조회
        Long count = queryFactory.select(paymentItem.count())
            .from(paymentItem)
            .innerJoin(paymentItem.receipt, receipt)
            .where(receipt.campaign.id.eq(campaignId))
            .fetchOne();
        return new PaymentDto(payments, count);
    }

    // campaignId에 해당하는 지출 내역 및 회원 리스트 조회
    public List<PaymentMemberDto> findPaymentMembersByCampaignId(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(PaymentMemberDto.class,
                    paymentItem.id,
                    member.id,
                    member.memberDetail.name))
            .from(paymentMember)
            .innerJoin(paymentMember.member, member)
            .innerJoin(paymentMember.paymentItem, paymentItem)
            .where(paymentItem.receipt.campaign.id.eq(campaignId))
            .fetch();
    }
}
