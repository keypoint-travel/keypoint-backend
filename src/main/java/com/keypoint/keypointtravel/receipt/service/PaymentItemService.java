package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import com.keypoint.keypointtravel.receipt.entity.PaymentMember;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import com.keypoint.keypointtravel.receipt.repository.PaymentItemRepository;
import com.keypoint.keypointtravel.receipt.repository.PaymentMemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentItemService {

    private final PaymentItemRepository paymentItemRepository;
    private final PaymentMemberRepository paymentMemberRepository;

    /**
     * 결제 항목 저장 함수
     *
     * @param receipt
     * @param useCase
     */
    @Transactional
    public void addPaymentItem(Receipt receipt, CreatePaymentItemUseCase useCase,
        List<Member> members) {
        // 1. 결제 상품 저장
        PaymentItem paymentItem = useCase.toEntity(receipt);
        paymentItemRepository.save(paymentItem);

        // 2. 결제 상품 참가자 저장
        List<PaymentMember> paymentMembers = new ArrayList<>();
        for (Member member : members) {
            PaymentMember paymentMember = PaymentMember.of(paymentItem, member);
            paymentMembers.add(paymentMember);
        }
        paymentMemberRepository.saveAll(paymentMembers);
    }
}
