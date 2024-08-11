package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadGuideService {

    private final GuideRepository guideRepository;

    /**
     * 이용 가이드 순서 번호 유효성 검사
     *
     * @param order
     */
    public boolean validateOrder(int order) {
        return guideRepository.existsByOrder(order);
    }
}
