package com.keypoint.keypointtravel.currency.service;

import com.keypoint.keypointtravel.currency.dto.ExchangeUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "exchangeRateApi",
    url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?data=AP01",
    fallback = CurrencyServiceFallback.class)
public interface CurrencyService {

    /**
     * 한국수출입은행 환율 정보를 조회하는 함수
     *
     * @Param authKey, searchDate
     * @Return 조회한 환율 정보
     */
    @GetMapping
    List<ExchangeUseCase> findExchange(
        @RequestParam("authkey") String authKey,
        @RequestParam("searchdate") String searchDate
    );
}
