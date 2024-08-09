package com.keypoint.keypointtravel.guide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.guide.entity.Guide;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    
}
