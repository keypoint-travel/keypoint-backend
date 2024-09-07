package com.keypoint.keypointtravel.visitedCountry.service;


import com.keypoint.keypointtravel.global.dto.useCase.SearchPageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse.SearchCampaignResponse;
import com.keypoint.keypointtravel.visitedCountry.dto.response.searchVisitedCountryResponse.SearchVisitedCountryResponse;
import com.keypoint.keypointtravel.visitedCountry.repository.VisitedCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitedCountryService {

    private final VisitedCountryRepository visitedCountryRepository;
    private final MemberDetailRepository memberDetailRepository;

    /**
     * 방문국가 검색 함수
     *
     * @param useCase
     * @return
     */
    public SearchVisitedCountryResponse findVisitedCountriesAndCampaigns(
        SearchPageAndMemberIdUseCase useCase) {
        LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                useCase.getMemberId());

        // 1. 캠페인 조회
        Page<SearchCampaignResponse> campaigns = visitedCountryRepository.findCampaignsByKeyword(useCase.getMemberId(), languageCode, useCase);

        // 2. 방문 도시 데이터 정리

        return SearchVisitedCountryResponse.of(null, campaigns);
    }
}
