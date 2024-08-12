package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReadGuideCustomRepository {

    Page<ReadGuideInAdminResponse> findGuidesInAdmin(Pageable pageable);

    ReadGuideDetailInAdminResponse findGuideDetailInAdmin(Long guideId);
}
