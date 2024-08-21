package com.keypoint.keypointtravel.receipt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreateReceiptUseCase;

import lombok.RequiredArgsConstructor;

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
            // 1. 유효성 확인 (주소, 경도, 위도)

            // 2. 참여 리스트가 모두 캠페인에 초대된 회원인지 확인

            // 3. 저장
            
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
