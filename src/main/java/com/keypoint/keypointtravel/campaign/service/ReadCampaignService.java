package com.keypoint.keypointtravel.campaign.service;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TotalBudgetDto;
import com.keypoint.keypointtravel.campaign.dto.response.PaymentInfo;
import com.keypoint.keypointtravel.campaign.dto.response.DetailsByCategoryResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReadCampaignService {


    private final MemberCampaignRepository memberCampaignRepository;

    private final CampaignBudgetRepository campaignBudgetRepository;

    private final CustomPaymentRepository customPaymentRepository;

    private final CurrencyRepository currencyRepository;

    /**
     * 캠페인 카테고리 별 결제 내역 조회 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return DetailsByCategoryResponse
     */
    @Transactional
    public DetailsByCategoryResponse findByCategory(FindPaymentUseCase useCase) {
        // 0. 캠페인에 소속되어 있는지 검증
        validateInCampaign(useCase);
        // 1. 캠페인 아이디를 통해 총 에산 조회
        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(useCase.getCampaignId());
        float total = campaignBudgets.stream().reduce(0f, (acc, budget) -> acc + budget.getAmount(), Float::sum);
        TotalBudgetDto totalBudget = new TotalBudgetDto(total, campaignBudgets.get(0).getCurrency());
        // 2. 캠페인 아이디를 통해 결제 항목 리스트 조회
        List<PaymentDto> paymentDtoList = customPaymentRepository.findPaymentList(useCase.getCampaignId());
        // 3. 결제 항목 별 참여 인원 리스트 조회
        List<Long> paymentItemIds = paymentDtoList.stream().map(PaymentDto::getPaymentItemId).toList();
        List<PaymentMemberDto> paymentMemberDtoList = customPaymentRepository.findMemberListByPayments(paymentItemIds);
        // 4. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
        if (useCase.getCurrencyType() != null) {
            List<Currency> currencies = currencyRepository.findAll();
            updateCurrency(useCase, totalBudget, paymentDtoList, currencies);
        }
        // 5. 카테고리 별 금액 계산 및 잔여 예산 계산
        HashMap<String, Float> categoryAmount = calculateCategoryAmount(paymentDtoList);
        float totalAmount = categoryAmount.values().stream().reduce(0f, Float::sum);
        float remainBudget = calculateRemainBudget(categoryAmount, totalBudget, totalAmount);
        // 6. 카테고리 별 비율 계산
        HashMap<String, Float> categoryPercentage = calculateCategoryPercentage(categoryAmount, totalAmount);
        // 7. 결제 항목 응답값 생성 및 응답
        return createResponse(totalBudget, totalAmount, remainBudget, categoryPercentage, paymentDtoList, paymentMemberDtoList);
    }

    /**
     * 캠페인 날짜 별 결제 내역 조회 함수
     *
     * @Param campaignId, currencyType, memberId useCase
     * @Return
     */
//    public void findByDate(FindPaymentUseCase useCase) {
//        // 0. 캠페인에 소속되어 있는지 검증
//        validateInCampaign(useCase);
//        // 1. 캠페인 아이디를 통해 총 에산 조회
//        List<CampaignBudget> campaignBudgets = campaignBudgetRepository.findAllByCampaignId(useCase.getCampaignId());
//        float total = campaignBudgets.stream().reduce(0f, (acc, budget) -> acc + budget.getAmount(), Float::sum);
//        TotalBudgetDto totalBudget = new TotalBudgetDto(total, campaignBudgets.get(0).getCurrency());
//        // 2. 캠페인 아이디를 통해 결제 항목 리스트 조회
//        List<PaymentDto> paymentDtoList = customPaymentRepository.findPaymentList(useCase.getCampaignId());
//        // 3. 결제 항목 별 참여 인원 리스트 조회
//        List<Long> paymentItemIds = paymentDtoList.stream().map(PaymentDto::getPaymentItemId).toList();
//        List<PaymentMemberDto> paymentMemberDtoList = customPaymentRepository.findMemberListByPayments(paymentItemIds);
//        // 4. 화폐 타입을 따로 지정할 경우 : totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
//        if (useCase.getCurrencyType() != null) {
//            List<Currency> currencies = currencyRepository.findAll();
//            updateCurrency(useCase, totalBudget, paymentDtoList, currencies);
//        }
//        // 5. 날짜 별 금액 계산 및 잔여 예산 계산
//        HashMap<String, Float> categoryAmount = calculateCategoryAmount(paymentDtoList);
//        float totalAmount = categoryAmount.values().stream().reduce(0f, Float::sum);
//        float remainBudget = calculateRemainBudget(categoryAmount, totalBudget, totalAmount);
//        // 6. 날짜 별 비율 계산
//        HashMap<String, Float> categoryPercentage = calculateCategoryPercentage(categoryAmount, totalAmount);
//    }

    // 캠페인에 소속되어 있는지 검증
    private void validateInCampaign(FindPaymentUseCase useCase) {
        if (!memberCampaignRepository.existsByCampaignIdAndMemberId(useCase.getCampaignId(), useCase.getMemberId())) {
            throw new GeneralException(CampaignErrorCode.NOT_EXISTED_CAMPAIGN);
        }
    }

    // totalBudget, paymentDtoList 의 화폐 타입과 금액을 변환
    private void updateCurrency(FindPaymentUseCase useCase, TotalBudgetDto totalBudget,
                                List<PaymentDto> paymentDtoList, List<Currency> currencies) {
        totalBudget.updateTotalBudget(
            convertCurrency(currencies, totalBudget.getTotalAmount(), totalBudget.getCurrencyType(), useCase.getCurrencyType()),
            useCase.getCurrencyType()
        );
        paymentDtoList.forEach(paymentDto -> paymentDto.updateBudget(
            convertCurrency(currencies, paymentDto.getAmount(), paymentDto.getCurrencyType(), useCase.getCurrencyType()),
            useCase.getCurrencyType()
        ));
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

    // 카테고리 별 금액 계산
    private HashMap<String, Float> calculateCategoryAmount(List<PaymentDto> paymentDtoList) {
        HashMap<String, Float> categoryAmount = new HashMap<>();
        paymentDtoList.forEach(paymentDto -> {
            String category = paymentDto.getCategory().getDescription();
            categoryAmount.put(category, categoryAmount.getOrDefault(
                category, 0f) + paymentDto.getAmount() * paymentDto.getQuantity());
        });
        return categoryAmount;
    }

    // 카테고리 별 금액 중 잔여 예산 계산
    private float calculateRemainBudget(HashMap<String, Float> categoryAmount, TotalBudgetDto totalBudget, float totalAmount) {
        float remainBudget = 0;
        if (totalBudget.getTotalAmount() - totalAmount > 0) {
            remainBudget = totalBudget.getTotalAmount() - totalAmount;
            categoryAmount.put("잔여 예산", remainBudget);
        }
        return remainBudget;
    }

    // 카테고리 별 비율 계산
    private HashMap<String, Float> calculateCategoryPercentage(HashMap<String, Float> categoryAmount, float totalAmount) {
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
        return categoryPercentage;
    }

    // 카테고리 별 결제 항목 응답값 생성 및 응답
    private DetailsByCategoryResponse createResponse(TotalBudgetDto totalBudget, float totalAmount, float remainBudget,
                                                     HashMap<String, Float> categoryPercentage, List<PaymentDto> paymentDtoList,
                                                     List<PaymentMemberDto> paymentMemberDtoList) {
        List<PaymentInfo> paymentInfoList = paymentDtoList.stream()
            .map(PaymentInfo::from)
            .collect(Collectors.toList());
        paymentInfoList.forEach(paymentInfo -> paymentInfo.addMembers(paymentMemberDtoList));
        return new DetailsByCategoryResponse(
            totalBudget.getCurrencyType().getCode(),
            Math.round(totalAmount * 100) / 100f,
            Math.round(remainBudget * 100) / 100f,
            categoryPercentage,
            paymentInfoList
        );
    }

    // 날짜 별 금액 계산
    private HashMap<String, Float> calculateDateAmount(List<PaymentDto> paymentDtoList) {
        HashMap<String, Float> dateAmount = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

        paymentDtoList.forEach(paymentDto -> {
            String formattedDate = paymentDto.getPaidAt().format(formatter);
            dateAmount.put(formattedDate, dateAmount.getOrDefault(
                formattedDate, 0f) + paymentDto.getAmount() * paymentDto.getQuantity());
        });
        return dateAmount;
    }

    // 날짜 별 금액 중 잔여 예산 계산
    private float calculateDateBudget(HashMap<String, Float> categoryAmount, TotalBudgetDto totalBudget, float totalAmount) {
        float remainBudget = 0;
        if (totalBudget.getTotalAmount() - totalAmount > 0) {
            remainBudget = totalBudget.getTotalAmount() - totalAmount;
            categoryAmount.put("잔여 예산", remainBudget);
        }
        return remainBudget;
    }

    // 날짜 별 비율 계산
    private HashMap<String, Float> calculateDatePercentage(HashMap<String, Float> categoryAmount, float totalAmount) {
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
        return categoryPercentage;
    }
}
