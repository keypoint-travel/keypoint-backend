package com.keypoint.keypointtravel.place.redis.repository;

import com.keypoint.keypointtravel.place.controller.RecentPlaceSearchController;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentPlaceSearchRepository extends
    CrudRepository<RecentPlaceSearchController, String> {

}
