package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreateReceiptUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateReceiptService {

    /**
     * 영수증 등록 함수
     *
     * @param useCase
     */
    public void addReceipt(CreateReceiptUseCase useCase) {
        try {

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
