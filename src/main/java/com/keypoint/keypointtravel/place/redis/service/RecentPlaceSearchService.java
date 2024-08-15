package com.keypoint.keypointtravel.place.redis.service;

import com.keypoint.keypointtravel.global.constants.PlaceConstants;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.place.dto.response.ReadPlaceSearchHistoryResponse;
import com.keypoint.keypointtravel.place.dto.useCase.DeleteRecentPlaceSearchUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.PlaceSearchUseCase;
import com.keypoint.keypointtravel.place.redis.entity.RecentPlaceSearch;
import com.keypoint.keypointtravel.place.redis.repository.RecentPlaceSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecentPlaceSearchService {

    private final RecentPlaceSearchRepository recentPlaceSearchRepository;

    /**
     * 최근 장소 검색어 추가
     *
     * @param useCase
     */
    @Transactional
    public void addSearchWord(PlaceSearchUseCase useCase) {
        try {
            String searchWord = useCase.getSearchWord();
            Long memberId = useCase.getMemberId();

            // 1. 이미 존재하면 날짜 업데이트, 없으면 저장
            RecentPlaceSearch recentPlaceSearch = recentPlaceSearchRepository
                .findByMemberIdAndSearchWord(memberId, searchWord)
                .orElse(RecentPlaceSearch.of(memberId, searchWord));

            recentPlaceSearch.setModifyAt();
            recentPlaceSearchRepository.save(recentPlaceSearch);

            // 3. 5개가 넘는 경우, 삭제
            List<RecentPlaceSearch> recentPlaceSearches = recentPlaceSearchRepository.findByMemberId(
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
    public void deleteSearchWord(DeleteRecentPlaceSearchUseCase useCase) {
        try {

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
    public List<ReadPlaceSearchHistoryResponse> getPlaceHistoryWords(MemberIdUseCase useCase) {
        try {
            return null;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
