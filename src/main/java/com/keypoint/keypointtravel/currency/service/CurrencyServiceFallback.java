package com.keypoint.keypointtravel.currency.service;

import com.keypoint.keypointtravel.currency.dto.ExchangeUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyServiceFallback implements CurrencyService {

    @Override
    public List<ExchangeUseCase> findExchange(String authKey, String searchDate) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}
