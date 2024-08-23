package com.keypoint.keypointtravel.place.redis.repository;

import com.keypoint.keypointtravel.place.redis.entity.RecentPlaceSearch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentPlaceSearchRepository extends
    CrudRepository<RecentPlaceSearch, String> {

    List<RecentPlaceSearch> findByMemberIdOrderByModifyAtDesc(Long memberId);

    Optional<RecentPlaceSearch> findByMemberIdAndPlaceId(Long memberId, Long placeId);
}
