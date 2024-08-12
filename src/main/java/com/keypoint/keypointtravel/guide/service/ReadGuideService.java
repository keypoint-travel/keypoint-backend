package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.useCase.GuideIdUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideInAdminUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideTranslationIdUseCase;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdAndPageableUseCase;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadGuideService {

    private final GuideRepository guideRepository;
    private final MemberDetailRepository memberDetailRepository;

    /**
     * 이용 가이드 순서 번호 유효성 검사
     *
     * @param order
     */
    public boolean validateOrder(int order) {
        return guideRepository.existsByOrder(order);
    }

    /**
     * guideId로 이용 가이드 조회
     *
     * @param guideId
     * @return
     */
    public Guide findGuideByGuideId(Long guideId) {
        return guideRepository.findById(guideId)
            .orElseThrow(() -> new GeneralException(GuideErrorCode.NOT_EXISTED_GUIDE));
    }

    /**
     * 전체 이용 가이드 리스트 조회 (기본값: 영어)
     *
     * @param useCase
     * @return
     */
    public Page<ReadGuideInAdminResponse> findGuidesInAdmin(ReadGuideInAdminUseCase useCase) {
        try {
            return guideRepository.findGuidesInAdmin(useCase.getPageable());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용 가이드 상세 조회 함수
     *
     * @param useCase
     * @return
     */
    public ReadGuideDetailInAdminResponse findGuideDetailInAdmin(GuideIdUseCase useCase) {
        try {
            return guideRepository.findGuideDetailInAdmin(useCase.getGuideId());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용가이드 전체 조회 함수 (사용자 언어로)
     *
     * @param useCase
     * @return
     */
    public Slice<ReadGuideResponse> findGuides(MemberIdAndPageableUseCase useCase) {
        try {
            LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId());
            return guideRepository.findGuides(languageCode, useCase.getPageable());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용 가이드 번역물 조회
     *
     * @param useCase
     * @return
     */
    public ReadGuideDetailResponse findGuideDetail(ReadGuideTranslationIdUseCase useCase) {
        try {
            return guideRepository.findGuideDetail(useCase.getGuideTranslationIds());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
