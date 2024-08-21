package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReadGuideCustomRepository {

    Page<ReadGuideInAdminResponse> findGuidesInAdmin(Pageable pageable);

    ReadGuideDetailInAdminResponse findGuideDetailInAdmin(Long guideId);

    Slice<ReadGuideResponse> findGuides(LanguageCode languageCode, Pageable pageable);

    ReadGuideDetailResponse findGuideDetail(Long guideTranslationIds);
}
