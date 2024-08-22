package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TotalBudgetDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.currency.entity.Currency;
import com.keypoint.keypointtravel.currency.repository.CurrencyRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.repository.CustomPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReadCampaignService {


    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CustomPaymentRepository customPaymentRepository;

    private final CurrencyRepository currencyRepository;

    /**
     * 캠페인 결제 항목을 카테고리 별 조회하는 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return
     */
    @Transactional
    public void findByCategory(FindPaymentUseCase useCase) {
        // 0. 캠페인에 소속되어 있는지 검증
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 1. 캠페인 아이디를 통해 총 에산 조회 TotalBudgetDto
        TotalBudgetDto totalBudget = campaignBudgetRepository.findTotalBudgetByCampaignId(useCase.getCampaignId());
        // 2. 캠페인 아이디를 통해 결제 항목 리스트 조회
        List<PaymentDto> paymentDtoList = customPaymentRepository.findPaymentList(useCase.getCampaignId());
        // 3. 결제 항목 별 참여 인원 리스트 조회
        List<Long> paymentItemIds = paymentDtoList.stream().map(PaymentDto::getPaymentItemId).toList();
        List<PaymentMemberDto> paymentMemberDtoList = customPaymentRepository.findMemberListByPayments(paymentItemIds);

        List<Currency> currencies = currencyRepository.findAll();
        // 4. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            totalBudget.updateTotalBudget(
                convertCurrency(currencies, totalBudget.getTotalAmount(), totalBudget.getCurrencyType(), useCase.getCurrencyType()),
                useCase.getCurrencyType()
            );
            paymentDtoList.forEach(paymentDto -> paymentDto.updateBudget(
                convertCurrency(currencies, paymentDto.getAmount(), paymentDto.getCurrencyType(), useCase.getCurrencyType()),
                useCase.getCurrencyType()
            ));
        }
        // 5. 카테고리 별 금액 계산 및 잔여 예산 계산
        HashMap<String, Float> categoryAmount = new HashMap<>();
        paymentDtoList.forEach(paymentDto -> {
            String category = paymentDto.getCategory().getDescription();
            categoryAmount.put(category, categoryAmount.getOrDefault(category, 0f) + paymentDto.getAmount());
        });
        float totalAmount = categoryAmount.values().stream().reduce(0f, Float::sum);
        if(totalBudget.getTotalAmount() - totalAmount > 0){
            float remainBudget = totalBudget.getTotalAmount() - totalAmount;
            categoryAmount.put("잔여 예산", remainBudget);
            totalAmount += remainBudget;
        }
        // 6. 카테고리 별 비율 계산
        HashMap<String, Float> categoryPercentage = new HashMap<>();
        // 최대 비율을 가지는 카테고리 선정
        String maxCategory = null;
        float maxPercentage = 0;
        for (HashMap.Entry<String, Float> entry : categoryAmount.entrySet()) {
            float percentage = (entry.getValue() / totalAmount) * 100;
            percentage = Math.round(percentage);
            categoryPercentage.put(entry.getKey(), percentage);
            if (percentage > maxPercentage) {
                maxPercentage = percentage;
                maxCategory = entry.getKey();
            }
        }
        float totalPercentage = categoryPercentage.values().stream().reduce(0f, Float::sum);
        // 비율 별 소수점 반올림 후 100%를 맞추기위해 최대 비율을 가진 카테고리에 차이를 더함
        float diff = 100 - totalPercentage;
        if (maxCategory != null) {
            categoryPercentage.put(maxCategory, maxPercentage + diff);
        }
    }

    private float convertCurrency(List<Currency> currencies, float amount, CurrencyType from, CurrencyType to) {

        Currency fromCurrency = currencies.stream()
            .filter(currency -> currency.getCur_unit().equals(from.getCode()))
            .findFirst().orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CURRENCY));
        Currency toCurrency = currencies.stream()
            .filter(currency -> currency.getCur_unit().equals(to.getCode()))
            .findFirst().orElseThrow(() -> new GeneralException(CampaignErrorCode.NOT_EXISTED_CURRENCY));
        return (float) (amount * fromCurrency.getExchange_rate() / toCurrency.getExchange_rate());
    }
}
