package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadNextGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.useCase.GuideIdUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideTranslationIdUseCase;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return guideRepository.existsByOrderAndIsDeletedFalse(order);
    }

    /**
     * guideId로 이용 가이드 조회
     *
     * @param guideId
     * @return
     */
    public Guide findGuideByGuideId(Long guideId) {
        return guideRepository.findByIdAndIsDeletedFalse(guideId)
            .orElseThrow(() -> new GeneralException(GuideErrorCode.NOT_EXISTED_GUIDE));
    }

    /**
     * 전체 이용 가이드 리스트 조회 (기본값: 영어)
     *
     * @param useCase
     * @return
     */
    public Page<ReadGuideInAdminResponse> findGuidesInAdmin(PageUseCase useCase) {
        try {
            return guideRepository.findGuidesInAdmin(useCase);
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
            ReadGuideDetailInAdminResponse response = guideRepository.findGuideDetailInAdmin(
                useCase.getGuideId());
            return response;
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
    public Page<ReadGuideResponse> findGuides(PageAndMemberIdUseCase useCase) {
        try {
            LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId());
            return guideRepository.findGuides(languageCode, useCase);
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
            LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId());

            // 1. 이용 가이드 조회
            ReadGuideDetailResponse response = guideRepository.findGuideDetail(
                useCase.getGuideTranslationIds());

            // 2. 다음 순서 이용 가이드 조회
            ReadNextGuideResponse nextGuideResponse = guideRepository.findNextGuide(
                response.getOrder(),
                languageCode
            );
            response.setNextGuide(nextGuideResponse);

            return response;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
