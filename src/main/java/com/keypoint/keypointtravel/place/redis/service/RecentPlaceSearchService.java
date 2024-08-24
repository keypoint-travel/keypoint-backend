package com.keypoint.keypointtravel.place.redis.service;

import com.keypoint.keypointtravel.global.constants.PlaceConstants;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.place.dto.response.ReadRecentPlaceSearchResponse;
import com.keypoint.keypointtravel.place.dto.useCase.DeleteRecentPlaceSearchUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.PlaceIdUseCase;
import com.keypoint.keypointtravel.place.redis.entity.RecentPlaceSearch;
import com.keypoint.keypointtravel.place.redis.repository.RecentPlaceSearchRepository;
import com.keypoint.keypointtravel.place.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecentPlaceSearchService {

    private final RecentPlaceSearchRepository recentPlaceSearchRepository;
    private final PlaceRepository placeRepository;

    /**
     * 최근 장소 검색어 추가
     *
     * @param useCase
     */
    @Transactional
    public void addSearchWord(PlaceIdUseCase useCase) {
        try {
            Long placeId = useCase.getPlaceId();
            Long memberId = useCase.getMemberId();

            // 1. 이미 존재하면 날짜 업데이트, 없으면 저장
            RecentPlaceSearch recentPlaceSearch = recentPlaceSearchRepository
                .findByMemberIdAndPlaceId(memberId, placeId)
                .orElse(RecentPlaceSearch.of(memberId, placeId));

            recentPlaceSearch.setModifyAt();
            recentPlaceSearchRepository.save(recentPlaceSearch);

            // 3. 5개가 넘는 경우, 삭제
            List<RecentPlaceSearch> recentPlaceSearches = recentPlaceSearchRepository.findByMemberIdOrderByModifyAtDesc(
                memberId);
            if (recentPlaceSearches.size() > PlaceConstants.MAX_PLACE_SEARCH_WORD_CNT) {
                recentPlaceSearchRepository.delete(
                    recentPlaceSearches.get(0)
                );
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 최근 검색어 삭제
     *
     * @param useCase
     */
    @Transactional
    public void deleteSearchWord(DeleteRecentPlaceSearchUseCase useCase) {
        try {
            recentPlaceSearchRepository.deleteById(useCase.getPlaceSearchHistoryId());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 최근 검색어 기록 조회
     *
     * @param useCase
     * @return
     */
    public List<ReadRecentPlaceSearchResponse> getPlaceHistoryWords(MemberIdUseCase useCase) {
        try {
            List<RecentPlaceSearch> recentPlaceSearches = recentPlaceSearchRepository.findByMemberIdOrderByModifyAtDesc(
                useCase.getMemberId()
            );

            return placeRepository.getRecentSearchPlaces(
                recentPlaceSearches,
                useCase.getMemberId()
            );
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
