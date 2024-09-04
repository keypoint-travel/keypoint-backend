package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.receipt.dto.dto.PaymentMemberDTO;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase.UpdatePaymentItemUseCase;
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
    private final MemberCampaignRepository memberCampaignRepository;

    /**
     * 결제 항목 저장 함수
     *
     * @param paymentItem
     * @param members
     */
    @Transactional
    public void addPaymentItem(
        PaymentItem paymentItem,
        List<Member> members
    ) {
        // 1. 결제 상품 저장
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
     * @param receipt    영수증 id
     * @param paymentItems 수정할 결제 항목 데이터
     */
    @Transactional
    public void updatePaymentItem(Receipt receipt, List<UpdatePaymentItemUseCase> paymentItems) {
        List<PaymentItem> savedItems = paymentItemRepository.findByReceiptId(receipt.getId());
        List<Member> invitedMembers = memberCampaignRepository.findMembersByCampaignId(
            receipt.getCampaign().getId()
        );
        List<Long> invitedMemberIds = invitedMembers.stream().map(Member::getId).toList();

        // 1. 생성 혹은 수정
        for (UpdatePaymentItemUseCase paymentItem : paymentItems) {
            // 참여 리스트가 모두 캠페인에 초대된 회원인지 확인
            if (!invitedMemberIds.containsAll(paymentItem.getMemberIds())) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                    "캠페인에 초대되지 않은 참가자가 포함되어 있습니다.");
            }

            if (paymentItem.getPaymentItemId() == null) { // 1-1. 데이터 생성
                List<Member> filteredMembers = invitedMembers.stream()
                    .filter(member -> paymentItem.getMemberIds().contains(member.getId()))
                    .toList();

                addPaymentItem(paymentItem.toEntity(receipt), filteredMembers);
            } else { // 1-2. 데이터 수정
                // 1-2-1. 결제 항목 수정
                long result = paymentItemRepository.updatePaymentItem(paymentItem);
                if (result < 0) {
                    throw new GeneralException(ReceiptError.NOT_EXISTED_PAYMENTITEM_IN_RECEIPT);
                }

                // 1-2-2. 결제 항목 참여 회원 수정
                updatePaymentMember(paymentItem.getPaymentItemId(), paymentItem.getMemberIds(),
                    invitedMembers);
            }
        }

        // 2. savedItems 에만 존재하는 item 인 경우 삭제
        deleteUnusedPaymentItems(savedItems, paymentItems);
    }

    /**
     * 사용하지 않는 결제 항목 삭제
     *
     * @param savedItems
     * @param paymentItems
     */
    @Transactional
    public void deleteUnusedPaymentItems(
        List<PaymentItem> savedItems,
        List<UpdatePaymentItemUseCase> paymentItems
    ) {
        List<Long> paymentItemIds = paymentItems.stream()
            .map(UpdatePaymentItemUseCase::getPaymentItemId)
            .toList();
        List<PaymentItem> itemsToDelete = savedItems.stream()
            .filter(item -> !paymentItemIds.contains(item.getId()))
            .toList();

        paymentItemRepository.deleteAllByIds(itemsToDelete);
    }

    /**
     * PaymentMember 수정
     *
     * @param paymentItemId 업데이트하려고 하는 PaymentItem 아이디
     * @param memberIds     새롭게 업데이트해야 하는 memberIds
     */
    @Transactional
    public void updatePaymentMember(
        Long paymentItemId,
        List<Long> memberIds,
        List<Member> invitedMembers
    ) {
        List<PaymentMemberDTO> dtos = paymentMemberRepository.findByPaymentItemId(paymentItemId);
        List<Long> savedMemberIds = dtos.stream().map(x -> x.getMemberId()).toList();

        List<Long> deletePaymentMemberIds = new ArrayList<>();
        List<Long> createMemberIds = memberIds.stream().filter(x -> !savedMemberIds.contains(x))
            .toList();

        // 삭제
        for (PaymentMemberDTO dto : dtos) {
            if (!memberIds.contains(dto.getMemberId())) {
                deletePaymentMemberIds.add(dto.getId());
            }
        }
        paymentMemberRepository.deleteAllById(deletePaymentMemberIds);

        // 생성
        List<Member> filteredMembers = invitedMembers.stream()
            .filter(member -> createMemberIds.contains(member.getId()))
            .toList();
        addPaymentMember(
            paymentItemRepository.getReferenceById(paymentItemId),
            filteredMembers
        );
    }
}
