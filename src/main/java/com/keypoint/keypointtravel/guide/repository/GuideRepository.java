package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    boolean existsByOrder(int order);
}
