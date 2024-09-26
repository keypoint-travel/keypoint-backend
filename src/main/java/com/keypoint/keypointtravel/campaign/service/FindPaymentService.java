package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentInfo;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import com.keypoint.keypointtravel.campaign.dto.response.PaymentResponse;
import com.keypoint.keypointtravel.campaign.dto.response.member.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.response.member.TotalAmountByMemberResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentsUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPercentangeUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.currency.entity.Currency;
import com.keypoint.keypointtravel.currency.repository.CurrencyRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.repository.CustomPaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPaymentService {

    private final CustomPaymentRepository customPaymentRepository;

    private final CurrencyRepository currencyRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    /**
     * 캠페인 상세 페이지 카테고리별 결제 항목 조회 함수
     *
     * @Param campaignId, memberId, currencyType, size, page, category useCase
     * @Return CampaignDetailsResponse
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> findPaymentsByCategory(FindPaymentsUseCase useCase, ReceiptCategory category) {
        // 1. campaignId와 category에 해당하는 page, size에 맞는 결제 항목 조회
        PaymentDto paymentDto = customPaymentRepository.findPaymentsByCategory(
            useCase.getCampaignId(), category, useCase.getSize(), useCase.getPage());
        // 2. 응답 값 생성 후 반환
        return createResponse(useCase, paymentDto);
    }

    /**
     * 캠페인 상세 페이지 날짜별 결제 항목 조회 함수
     *
     * @Param campaignId, memberId, currencyType, size, page, date useCase
     * @Return CampaignDetailsResponse
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> findPaymentsByDate(FindPaymentsUseCase useCase, String date) {
        // 1. campaignId와 date에 해당하는 page, size에 맞는 결제 항목 조회
        PaymentDto paymentDto = customPaymentRepository.findPaymentsByDate(
            useCase.getCampaignId(), date, useCase.getSize(), useCase.getPage());
        // 2. 응답 값 생성 후 반환
        return createResponse(useCase, paymentDto);
    }

    /**
     * 캠페인 상세 페이지 금액별 결제 항목 조회 함수
     *
     * @Param campaignId, memberId, currencyType, size, page, direction useCase
     * @Return CampaignDetailsResponse
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> findPaymentsByDate(FindPaymentsUseCase useCase, Sort.Direction direction) {
        // 1. campaignId에 해당하는 page, size에 맞는 결제 항목 조회
        PaymentDto paymentDto = customPaymentRepository.findPaymentsByPrice(
            useCase.getCampaignId(), direction, useCase.getSize(), useCase.getPage());
        // 2. 응답 값 생성 후 반환
        return createResponse(useCase, paymentDto);
    }

    /**
     * 캠페인 상세 페이지 회원별 결제 항목 조회 함수
     *
     * @Param campaignId, memberId, currencyType, size, page useCase
     * @Return CampaignDetailsResponse
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> findPaymentsByMember(FindPaymentsUseCase useCase) {
        // 0. 캠페인에 참여한 회원인지 확인
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 1. campaignId와 memberId에 해당하는 page, size에 맞는 결제 항목 조회
        PaymentDto paymentDto = customPaymentRepository.findPaymentsByMember(
            useCase.getCampaignId(), useCase.getMemberId(), useCase.getSize(), useCase.getPage());
        for(PaymentInfo payment : paymentDto.getPaymentList()){
            payment.updateBudget(Math.round(payment.getAmount()));
        }
        // 2. 응답 값 생성 후 반환
        return createResponse(useCase, paymentDto);
    }

    /**
     * 캠페인 상세 페이지 회원별 총 결제 금액 조회 함수
     *
     * @Param campaignId
     * @Return TotalAmountByMemberResponse
     */
    @Transactional(readOnly = true)
    public TotalAmountByMemberResponse findTotalPaymentsByAllMember(FindPercentangeUseCase useCase) {
        // 1. campaignId 에 해당하는 회원별 총 금액 조회
        List<AmountByMemberDto> dtoList = customPaymentRepository.findAmountAllMember(useCase.getCampaignId());
        // 2. 화폐 종류 조회
        CurrencyType currencyType = campaignBudgetRepository.findCurrencyByCampaignId(
            useCase.getCampaignId()
        ).orElse(CurrencyType.USD); // TODO 변경 필요
        // 3. 회원별 금액 통합 및 응답 값 변환
        List<MemberInfo> members = new ArrayList<>();
        for (int i = 0; i < dtoList.size(); i++) {
            if(i == 0){
                members.add(MemberInfo.from(dtoList.get(i)));
            } else {
                if(Objects.equals(dtoList.get(i - 1).getMemberId(), dtoList.get(i).getMemberId())){
                    members.get(members.size() - 1).addAmount(dtoList.get(i).getAmount());
                } else {
                    members.add(MemberInfo.from(dtoList.get(i)));
                }
            }
        }
        // 4. 조회된 결제 항목을 currencyType에 맞게 변환
        if (useCase.getCurrencyType() != null && !members.isEmpty()) {
            List<Currency> currencies = currencyRepository.findAll();
            for (MemberInfo memberInfo : members) {
                memberInfo.updateAmount(
                    convertCurrency(currencies, memberInfo.getAmount(), currencyType, useCase.getCurrencyType())
                );
            }
            currencyType = useCase.getCurrencyType();
        }
        // 5. 응답
        return new TotalAmountByMemberResponse(currencyType, members);
    }

    // totalBudget, categoryAmounts 의 화폐 타입과 금액을 변환
    private void updateCurrency(CurrencyType fromCurrency, CurrencyType toCurrency,
                                List<? extends AmountDto> amounts, List<Currency> currencies) {
        for (AmountDto amount : amounts) {
            amount.updateBudget(
                convertCurrency(currencies, amount.getAmount(), fromCurrency, toCurrency)
            );
        }
    }

    private float convertCurrency(List<Currency> currencies, float amount, CurrencyType from, CurrencyType to) {
        Currency fromCurrency = currencies.stream()
            .filter(currency -> currency.getCur_unit().equals(from.getCode()))
            .findFirst().orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CURRENCY));
        Currency toCurrency = currencies.stream()
            .filter(currency -> currency.getCur_unit().equals(to.getCode()))
            .findFirst().orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CURRENCY));
        return (float) Math.round(amount * fromCurrency.getExchange_rate() / toCurrency.getExchange_rate() * 100) / 100f;
    }

    private Page<PaymentResponse> createResponse(FindPaymentsUseCase useCase, PaymentDto paymentDto) {
        // 1. 조회된 결제 항목을 currencyType에 맞게 변환
        if (useCase.getCurrencyType() != null && !paymentDto.getPaymentList().isEmpty()) {
            List<Currency> currencies = currencyRepository.findAll();
            updateCurrency(paymentDto.getPaymentList().get(0).getCurrencyType(), useCase.getCurrencyType(),
                paymentDto.getPaymentList(), currencies);
        }
        // 2. 결제 항목별 참여 인원 리스트 조회
        List<PaymentMemberDto> paymentMemberList = customPaymentRepository.findPaymentMembersByCampaignId(
            useCase.getCampaignId());
        // 3. 응답
        List<PaymentResponse> responses = new ArrayList<>();
        for (PaymentInfo payment : paymentDto.getPaymentList()) {
            responses.add(PaymentResponse.of(payment, paymentMemberList));
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(
            useCase.getPage() > 0 ? useCase.getPage() - 1 : 0, useCase.getSize() > 0 ? useCase.getSize() : 1, sort);
        return new PageImpl<>(responses, pageable, paymentDto.getTotalCount());
    }
}
