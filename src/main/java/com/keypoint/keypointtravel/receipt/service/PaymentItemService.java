package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase.UpdatePaymentItemUseCase;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import com.keypoint.keypointtravel.receipt.entity.PaymentMember;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import com.keypoint.keypointtravel.receipt.repository.PaymentItemRepository;
import com.keypoint.keypointtravel.receipt.repository.PaymentMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        addPaymentMember(paymentItem, members);

    }

    @Transactional
    public void addPaymentMember(PaymentItem paymentItem, List<Member> members) {
        List<PaymentMember> paymentMembers = new ArrayList<>();
        for (Member member : members) {
            PaymentMember paymentMember = PaymentMember.of(paymentItem, member);
            paymentMembers.add(paymentMember);
        }
        paymentMemberRepository.saveAll(paymentMembers);
    }

    /**
     * 결제 항목 수정 함수
     *
     * @param receiptId    영수증 id
     * @param paymentItems 수정할 결제 항목 데이터
     */
    @Transactional
    public void updatePaymentItem(Receipt receipt, List<UpdatePaymentItemUseCase> paymentItems) {
        List<PaymentItem> savedItems = paymentItemRepository.findByReceiptId(receipt.getId());

        // paymentItemId가 null인 경우 데이터 생성
        List<PaymentItem> newItems = paymentItems.stream()
                .filter(item -> item.getPaymentItemId() == null)
                .map(item -> item.toEntity(receipt))
                .collect(Collectors.toList());
        paymentItemRepository.saveAll(newItems); // TODO PaymentMember 저장하도록 수정

        // savedItems에 paymentItemId가 존재하는 경우 데이터를 업데이트
        for (UpdatePaymentItemUseCase paymentItem : paymentItems) {
            if (paymentItem.getPaymentItemId() != null) {
                Optional<PaymentItem> existingItemOpt = savedItems.stream()
                        .filter(item -> item.getId().equals(paymentItem.getPaymentItemId()))
                        .findFirst();

                if (existingItemOpt.isPresent()) {
                    PaymentItem existingItem = existingItemOpt.get();
                    existingItem.setItemName(paymentItem.getItemName());
                    existingItem.setAmount(paymentItem.getAmount());
                    existingItem.setQuantity(paymentItem.getQuantity());
                    existingItem.setMemberIds(paymentItem.getMemberIds());
                    paymentItemRepository.save(existingItem);
                } else {
                    throw new GeneralException(ReceiptError.NOT_EXISTED_PAYMENTITEM_IN_RECEIPT);
                }
            }
        }

        // savedItems에만 존재하는 item인 경우 삭제
        List<Long> paymentItemIds = paymentItems.stream()
                .map(UpdatePaymentItemUseCase::getPaymentItemId)
                .toList();

        List<PaymentItem> itemsToDelete = savedItems.stream()
                .filter(item -> !paymentItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        paymentItemRepository.deleteAll(itemsToDelete);
    }
}
