package com.keypoint.keypointtravel.receipt.redis.service;

import com.keypoint.keypointtravel.receipt.redis.respository.TempReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempReceiptService {

    private final TempReceiptRepository tempReceiptRepository;
}
