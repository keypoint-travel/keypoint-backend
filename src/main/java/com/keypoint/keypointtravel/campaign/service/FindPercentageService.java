package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TotalBudgetDto;
import com.keypoint.keypointtravel.campaign.dto.response.category.CategoryPercentageResponse;
import com.keypoint.keypointtravel.campaign.dto.response.category.PercentageByCategory;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.currency.entity.Currency;
import com.keypoint.keypointtravel.currency.repository.CurrencyRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.repository.CustomPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindPercentageService {

    private final CustomPaymentRepository customPaymentRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CurrencyRepository currencyRepository;

    /**
     * 캠페인 카테고리 별 지출 퍼센트 조회 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return 사용 금액, 잔여 예산, 화폐 단위, List [카테고리, 가격, 비율] CategoryPercentageResponse
     */
    @Transactional(readOnly = true)
    public CategoryPercentageResponse findCategoryPercentage(FindPaymentUseCase useCase) {
        // 1. 캠페인 아이디를 통해 캠페인 생성 시 지정한 총 예산 조회
        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(useCase.getCampaignId());
        float campaignAmount = campaignBudgets.stream().reduce(0f, (acc, budget) -> acc + budget.getAmount(), Float::sum);
        TotalBudgetDto totalBudget = new TotalBudgetDto(campaignAmount, campaignBudgets.get(0).getCurrency());
        // 2. 캠페인 아이디를 통해 카테고리 별 사용한 금액 조회
        List<AmountByCategoryDto> categoryAmounts = customPaymentRepository.findAmountByCategory(useCase.getCampaignId());
        // 3. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            updateCurrency(useCase, totalBudget, categoryAmounts, currencies);
        }
        // 4. 잔여 예산을 포함한 카테고리 별 금액 종합 및 잔여 예산 계산
        HashMap<String, Float> returnAmounts = new HashMap<>();
        categoryAmounts.forEach(amount -> returnAmounts.put(amount.getCategory().getDescription(), amount.getAmount()));
        float usedTotalAmount = categoryAmounts.stream().reduce(0f, (acc, amount) -> acc + amount.getAmount(), Float::sum);
        float remainBudget = calculateRemainBudget(returnAmounts, totalBudget, usedTotalAmount);
        usedTotalAmount += remainBudget;
        // 5. 카테고리 별 비율 계산 최대 비율을 가지는 카테고리 선정
        List<PercentageByCategory> percentages = calculateCategoryPercentage(returnAmounts, usedTotalAmount);
        // 6. 응답
        return new CategoryPercentageResponse(
            totalBudget.getCurrencyType().getCode(),
            Math.round((usedTotalAmount - remainBudget) * 100) / 100f,
            Math.round(remainBudget * 100) / 100f,
            percentages
        );
    }

    // totalBudget, categoryAmounts 의 화폐 타입과 금액을 변환
    private void updateCurrency(FindPaymentUseCase useCase, TotalBudgetDto totalBudget,
                                List<AmountByCategoryDto> categoryAmounts, List<Currency> currencies) {
        CurrencyType currencyType = totalBudget.getCurrencyType();
        totalBudget.updateTotalBudget(
            convertCurrency(currencies, totalBudget.getTotalAmount(), currencyType, useCase.getCurrencyType()),
            useCase.getCurrencyType()
        );
        for (AmountByCategoryDto amount : categoryAmounts) {
            amount.updateBudget(
                convertCurrency(currencies, amount.getAmount(), currencyType, useCase.getCurrencyType())
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
        return (float) (amount * fromCurrency.getExchange_rate() / toCurrency.getExchange_rate());
    }

    private float calculateRemainBudget(HashMap<String, Float> returnAmounts, TotalBudgetDto totalBudget, float totalAmount) {
        float remainBudget = 0;
        if (totalBudget.getTotalAmount() - totalAmount > 0) {
            remainBudget = totalBudget.getTotalAmount() - totalAmount;
            returnAmounts.put("잔여 예산", remainBudget);
        }
        return remainBudget;
    }

    private List<PercentageByCategory> calculateCategoryPercentage(HashMap<String, Float> amounts, float totalAmount) {
        List<PercentageByCategory> percentages = new ArrayList<>();
        // 최대 비율을 가지는 카테고리 선정
        String maxCategory = null;
        float maxPercentage = 0;
        for (HashMap.Entry<String, Float> entry : amounts.entrySet()) {
            float percentage = (entry.getValue() / totalAmount) * 100;
            percentage = Math.round(percentage);
            percentages.add(new PercentageByCategory(entry.getKey(), Math.round(entry.getValue() * 100) / 100f, percentage));
            if (percentage > maxPercentage) {
                maxPercentage = percentage;
                maxCategory = entry.getKey();
            }
        }
        float totalPercentage = percentages.stream().reduce(0f, (acc, percentage) -> acc + percentage.getPercentage(), Float::sum);
        // 비율 별 소수점 반올림 후 100%를 맞추기위해 최대 비율을 가진 카테고리에 차이를 더함
        float diff = 100 - totalPercentage;
        if (maxCategory != null) {
            for (PercentageByCategory percentage : percentages) {
                if (percentage.getCategory().equals(maxCategory)) {
                    percentage.updatePercentage(percentage.getPercentage() + diff);
                }
            }
        }
        return percentages;
    }
}
