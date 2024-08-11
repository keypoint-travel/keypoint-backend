package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideUseCase;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateGuideService {
    private final GuideRepository guideRepository;

  /**
   * 이용 가이드 추가함수 - 처음 등록은 영어 버전으로 등록
   *
   * @param useCase
   */
  public void addGuide(CreateGuideUseCase useCase) {
  }

  /**
   * 이용 가이드 다른 언어 버전 번역물 등록 함수
   *
   * @param useCase
   */
  public void addGuideTranslation(CreateGuideTranslationUseCase useCase) {
  }
}
