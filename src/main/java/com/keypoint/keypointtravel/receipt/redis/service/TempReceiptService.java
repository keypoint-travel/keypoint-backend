package com.keypoint.keypointtravel.receipt.redis.service;

import com.keypoint.keypointtravel.external.azure.dto.useCase.WholeReceiptUseCase;
import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
import com.keypoint.keypointtravel.receipt.redis.respository.TempReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempReceiptService {

    private final TempReceiptRepository tempReceiptRepository;

    /**
     * 임시 영수증 데이터 저장
     *
     * @param dto 영수증 데이터
     * @return
     */
    @Transactional
    public String addTempReceipt(WholeReceiptUseCase dto) {
        TempReceipt tempReceipt = dto.toEntity();
        tempReceiptRepository.save(tempReceipt);

        return tempReceipt.getId();
    }

    /**
     * 영수증 찾는 함수
     *
     * @param id
     * @return
     */
    public TempReceipt findTempReceiptById(String id) {
        if (id == null) {
            return null;
        }

        return tempReceiptRepository.findById(id)
            .orElse(null);
    }
}
