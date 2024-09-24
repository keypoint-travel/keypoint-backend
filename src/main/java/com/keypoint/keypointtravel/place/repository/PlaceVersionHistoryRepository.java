package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.place.entity.PlaceVersionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceVersionHistoryRepository extends JpaRepository<PlaceVersionHistory, Long> {

  boolean existsByVersion(String version);
}
