package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadNextGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReadGuideCustomRepository {

    Page<ReadGuideInAdminResponse> findGuidesInAdmin(Pageable pageable);

    ReadGuideDetailInAdminResponse findGuideDetailInAdmin(Long guideId);

    Slice<ReadGuideResponse> findGuides(LanguageCode languageCode, PageAndMemberIdUseCase useCase);

    ReadGuideDetailResponse findGuideDetail(Long guideTranslationIds);

    ReadNextGuideResponse findNextGuide(int order, LanguageCode languageCode);
}
