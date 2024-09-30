package com.keypoint.keypointtravel.currency.service;

import com.keypoint.keypointtravel.currency.dto.ExchangeUseCase;
import com.keypoint.keypointtravel.currency.entity.Currency;
import com.keypoint.keypointtravel.currency.repository.CurrencyRepository;
import com.keypoint.keypointtravel.currency.utils.DateUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;
    @Value("${key.exchange-rate.key}")
    private String key;

    // 월 ~ 금 14시 00분 부터 5분 주기로 55분까지 12회 진행
    @Scheduled(cron = "0 0/1 * * * MON-FRI", zone = "Asia/Seoul")
    public void updateCurrency() {
        try {
            String today = DateUtils.getToday();
            List<ExchangeUseCase> exchanges = currencyService.findExchange(key, today);
            List<Currency> currencies = new ArrayList<>();
            for (ExchangeUseCase exchange : exchanges) {
                processExchange(exchange, currencies, today);
            }
            LogUtils.writeInfoLog("updateCurrency", "Save exchange rate to DB");
            currencyRepository.saveAll(currencies);
        } catch (Exception exception) {
            LogUtils.writeErrorLog("updateCurrency", "Failed to update exchange rate : " + exception.getMessage());
        }
    }

    private void processExchange(ExchangeUseCase exchange, List<Currency> currencies, String today) {
        if (isTargetCurrency(exchange.getCur_unit())) {
            String cur_unit = exchange.getCur_unit();
            String cur_name = exchange.getCur_nm();
            double exchangeRate = Double.parseDouble(exchange.getDeal_bas_r().replace(",", ""));
            if (exchange.getCur_unit().equals("JPY(100)")) {
                cur_unit = "JPY";
                cur_name = "일본 엔";
                exchangeRate = Math.round(exchangeRate) / 100.0;
            }
            Currency currency = new Currency(cur_unit, cur_name, exchangeRate, today);
            currencies.add(currency);
        }
        currencies.add(new Currency("KRW", "한국 원", 1.0, today));
    }

    // 엔과 달러만 저장
    private boolean isTargetCurrency(String cur_unit) {
        return cur_unit.equals("JPY(100)") || cur_unit.equals("USD");
    }
}
