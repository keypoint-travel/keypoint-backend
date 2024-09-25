package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.AmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TotalBudgetDto;
import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.MemberAmountByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.TotalAmountDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignReportPrice;
import com.keypoint.keypointtravel.campaign.dto.response.PercentageResponse;
import com.keypoint.keypointtravel.campaign.dto.response.PercentageByCategory;
import com.keypoint.keypointtravel.campaign.dto.response.member.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.response.member.PercentageByMemberResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPercentangeUseCase;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.campaign.repository.CampaignBudgetRepository;
import com.keypoint.keypointtravel.campaign.repository.MemberCampaignRepository;
import com.keypoint.keypointtravel.currency.entity.Currency;
import com.keypoint.keypointtravel.currency.repository.CurrencyRepository;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.error.CampaignErrorCode;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.repository.CustomPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FindPercentageService {

    private final CustomPaymentRepository customPaymentRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CurrencyRepository currencyRepository;

    private final MemberCampaignRepository memberCampaignRepository;

    /**
     * 캠페인 카테고리별 지출 퍼센트 조회 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return 사용 금액, 잔여 예산, 화폐 단위, List [카테고리, 가격, 비율] CategoryPercentageResponse
     */
    @Transactional(readOnly = true)
    public PercentageResponse findCategoryPercentage(FindPercentangeUseCase useCase) {
        // 1. 캠페인 아이디를 통해 캠페인 생성 시 지정한 총 예산 조회
        TotalBudgetDto totalBudget = findTotalBudget(useCase.getCampaignId());
        // 2. 캠페인 아이디를 통해 카테고리별 사용한 금액 조회
        List<AmountByCategoryDto> categoryAmounts = customPaymentRepository.findAmountByCategory(useCase.getCampaignId());
        // 3. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            updateCurrency(useCase, totalBudget, totalBudget.getCurrencyType(), categoryAmounts, currencies);
        }
        // 4. 잔여 예산을 포함한 카테고리별 금액 종합 및 잔여 예산 계산
        HashMap<String, Float> returnAmounts = new HashMap<>();
        categoryAmounts.forEach(amount -> returnAmounts.put(amount.getCategory().getDescription(), amount.getAmount()));
        float usedTotalAmount = categoryAmounts.stream().reduce(0f, (acc, amount) -> acc + amount.getAmount(), Float::sum);
        float remainBudget = calculateRemainBudget(returnAmounts, totalBudget, usedTotalAmount);
        usedTotalAmount += remainBudget;
        // 5. 카테고리별 비율 계산 최대 비율을 가지는 카테고리 선정
        List<PercentageByCategory> percentages = calculateCategoryPercentage(returnAmounts, usedTotalAmount);
        // 사용되지 않은 캠페인 예산 0원으로 추가
        addBudgetPercentage(percentages);
        // 6. 응답
        return new PercentageResponse(
            totalBudget.getCurrencyType().getCode(),
            Math.round((usedTotalAmount - remainBudget) * 100) / 100f,
            Math.round(remainBudget * 100) / 100f,
            percentages
        );
    }

    /**
     * 캠페인 날짜별 지출 퍼센트 조회 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return 사용 금액, 잔여 예산, 화폐 단위, List [날짜, 가격, 비율] DatePercentageResponse
     */
    @Transactional(readOnly = true)
    public PercentageResponse findDatePercentage(FindPercentangeUseCase useCase) {
        // 1. 캠페인 아이디를 통해 캠페인 생성 시 지정한 총 예산 조회
        TotalBudgetDto totalBudget = findTotalBudget(useCase.getCampaignId());
        // 2. 캠페인 아이디를 통해 날짜별 사용한 금액 조회
        List<AmountByDateDto> dateAmounts = customPaymentRepository.findAmountByDate(useCase.getCampaignId());
        // 3. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            updateCurrency(useCase, totalBudget, totalBudget.getCurrencyType(), dateAmounts, currencies);
        }
        // 4. 잔여 예산을 포함한 날짜별 금액 종합 및 잔여 예산 계산
        HashMap<String, Float> returnAmounts = new HashMap<>();
        dateAmounts.forEach(amount -> returnAmounts.put(amount.getDate(), amount.getAmount()));
        float usedTotalAmount = dateAmounts.stream().reduce(0f, (acc, amount) -> acc + amount.getAmount(), Float::sum);
        float remainBudget = calculateRemainBudget(returnAmounts, totalBudget, usedTotalAmount);
        usedTotalAmount += remainBudget;
        // 5. 날짜별 비율 계산 최대 비율을 가지는 카테고리 선정
        List<PercentageByCategory> percentages = calculateCategoryPercentage(returnAmounts, usedTotalAmount);
        // 6. 응답
        return new PercentageResponse(
            totalBudget.getCurrencyType().getCode(),
            Math.round((usedTotalAmount - remainBudget) * 100) / 100f,
            Math.round(remainBudget * 100) / 100f,
            percentages
        );
    }

    /**
     * 캠페인 회원별 지출 퍼센트 조회 함수
     *
     * @Param campaignId, memberId useCase
     * @Return
     */
    public PercentageByMemberResponse findMemberPercentage(FindPercentangeUseCase useCase) {
        // 0. 캠페인에 소속되어 있는지 검증
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
        // 1. 총 예산, 총 사용 금액, 총 회원 수, 화폐 타입 조회
        TotalAmountDto dto = customPaymentRepository.findTotalAmountByCampaignId(useCase.getCampaignId());
        if (dto == null) {
            return new PercentageByMemberResponse(null, new ArrayList<>());
        }
        float totalAmount = dto.getTotalAmount();
        float allMemberUsedAmount = dto.getUsedAmount();
        // 2. 캠페인 내 회원 아이디에 해당하는 사용 금액 조회
        List<MemberAmountByCategoryDto> memberAmounts = customPaymentRepository
            .findAmountByMember(useCase.getCampaignId(), useCase.getMemberId());
        // 3. 잔여 예산을 포함한 날짜 별 금액 종합 및 잔여 예산 계산
        float remainingBudget = 0f;
        float usedTotalAmount = 0f;
        HashMap<String, Float> returnAmounts = new HashMap<>();
        for (MemberAmountByCategoryDto amount : memberAmounts) {
            usedTotalAmount += amount.getAmount();
            returnAmounts.put(amount.getCategory().getDescription(),
                returnAmounts.getOrDefault(amount.getCategory().getDescription(), 0f) + amount.getAmount());
        }
        if (totalAmount - allMemberUsedAmount > 0) {
            remainingBudget = (totalAmount - allMemberUsedAmount) / dto.getTotalMember();
            usedTotalAmount += remainingBudget;
        }
        returnAmounts.put("remaining_budget", remainingBudget);
        // 4. 조회된 결제 항목을 currencyType에 맞게 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            usedTotalAmount = convertCurrency(currencies, usedTotalAmount, dto.getCurrency(), useCase.getCurrencyType());
            returnAmounts.replaceAll(
                (k, v) -> convertCurrency(currencies, v, dto.getCurrency(), useCase.getCurrencyType()));
            dto.updateCurrency(useCase.getCurrencyType());
        }
        // 5. 카테고리 별 비율 계산 최대 비율을 가지는 카테고리 선정 및 응답
        List<PercentageByCategory> percentages = calculateCategoryPercentage(returnAmounts, usedTotalAmount);
        // 사용되지 않은 캠페인 예산 0원으로 추가
        addBudgetPercentage(percentages);
        // 6. 응답
        return new PercentageByMemberResponse(dto.getCurrency().getCode(), percentages);
    }

    /**
     * 캠페인 레포트 조회 함수
     *
     * @Param campaignId, memberId useCase
     * @Return
     */
    public CampaignReportPrice findCampaignReport(FindPercentangeUseCase useCase) {
        // 1. 캠페인 아이디를 통해 캠페인 생성 시 지정한 총 예산 조회
        TotalBudgetDto totalBudget = findTotalBudget(useCase.getCampaignId());
        // 2. 캠페인 아이디를 통해 카테고리 별 사용한 금액 조회
        List<AmountByCategoryDto> categoryAmounts = customPaymentRepository.findAmountByCategory(useCase.getCampaignId());
        // 3. 캠페인 아이디를 통해 날짜별 사용한 금액 조회
        List<AmountByDateDto> dateAmounts = customPaymentRepository.findAmountByDate(useCase.getCampaignId());
        // 4-1. 캠페인 아이디를 통해 회원별 총 금액 조회
        List<AmountByMemberDto> memberAmounts = customPaymentRepository.findAmountAllMember(useCase.getCampaignId());
        // 4-2. 회원별 금액 통합 및 응답 값 변환
        List<MemberInfo> members = new ArrayList<>();
        for (int i = 0; i < memberAmounts.size(); i++) {
            if (i == 0) {
                members.add(MemberInfo.from(memberAmounts.get(i)));
            } else {
                if (Objects.equals(memberAmounts.get(i - 1).getMemberId(), memberAmounts.get(i).getMemberId())) {
                    members.get(members.size() - 1).addAmount(memberAmounts.get(i).getAmount());
                } else {
                    members.add(MemberInfo.from(memberAmounts.get(i)));
                }
            }
        }
        // 5. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            CurrencyType fromCurrency = totalBudget.getCurrencyType();
            updateCurrency(useCase, totalBudget, fromCurrency, categoryAmounts, currencies);
            updateCurrency(useCase, totalBudget, fromCurrency, dateAmounts, currencies);
            for (MemberInfo memberInfo : members) {
                memberInfo.updateAmount(
                    convertCurrency(currencies, memberInfo.getAmount(), fromCurrency, useCase.getCurrencyType())
                );
            }
        }
        // 6. 잔여 예산을 포함한 카테고리 별 금액 종합 및 잔여 예산 계산
        HashMap<String, Float> returnAmounts = new HashMap<>();
        categoryAmounts.forEach(amount -> returnAmounts.put(amount.getCategory().getDescription(), amount.getAmount()));
        float usedTotalAmount = categoryAmounts.stream().reduce(0f, (acc, amount) -> acc + amount.getAmount(), Float::sum);
        float remainBudget = calculateRemainBudget(returnAmounts, totalBudget, usedTotalAmount);
        usedTotalAmount += remainBudget;
        // 7. 카테고리별 비율 계산 최대 비율을 가지는 카테고리 선정
        List<PercentageByCategory> percentages = calculateCategoryPercentage(returnAmounts, usedTotalAmount);
        // 사용되지 않은 캠페인 예산 0원으로 추가
        addBudgetPercentage(percentages);
        // 8. 응답
        return new CampaignReportPrice(
            totalBudget.getCurrencyType().getCode(),
            Math.round((usedTotalAmount - remainBudget) * 100) / 100f,
            Math.round(remainBudget * 100) / 100f,
            percentages,
            dateAmounts,
            members
        );
    }

    private TotalBudgetDto findTotalBudget(Long campaignId) {
        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(campaignId);
        TotalBudgetDto totalBudget;
        if (campaignBudgets.isEmpty()) {
            totalBudget = new TotalBudgetDto(0, CurrencyType.USD);
        } else {
            float campaignAmount = campaignBudgets.stream().reduce(0f, (acc, budget) -> acc + budget.getAmount(), Float::sum);
            totalBudget = new TotalBudgetDto(campaignAmount, campaignBudgets.get(0).getCurrency());
        }
        return totalBudget;
    }

    // totalBudget, categoryAmounts 의 화폐 타입과 금액을 변환
    private void updateCurrency(FindPercentangeUseCase useCase, TotalBudgetDto totalBudget, CurrencyType fromCurrency,
                                List<? extends AmountDto> amounts, List<Currency> currencies) {
        totalBudget.updateTotalBudget(
            convertCurrency(currencies, totalBudget.getTotalAmount(), fromCurrency, useCase.getCurrencyType()),
            useCase.getCurrencyType()
        );
        for (AmountDto amount : amounts) {
            amount.updateBudget(
                convertCurrency(currencies, amount.getAmount(), fromCurrency, useCase.getCurrencyType())
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

    private float calculateRemainBudget(HashMap<String, Float> returnAmounts, TotalBudgetDto totalBudget, float totalAmount) {
        float remainBudget = 0;
        if (totalBudget.getTotalAmount() - totalAmount > 0) {
            remainBudget = totalBudget.getTotalAmount() - totalAmount;
        }
        returnAmounts.put("remaining_budget", remainBudget);
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

    private void addBudgetPercentage(List<PercentageByCategory> percentages) {
        for (ReceiptCategory category : ReceiptCategory.values()) {
            if (percentages.stream().noneMatch(p -> p.getCategory().equals(category.getDescription()))) {
                percentages.add(new PercentageByCategory(category.getDescription(), 0, 0));
            }
        }
    }
}
