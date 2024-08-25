package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.entity.Guide;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long>,
    ReadGuideCustomRepository, UpdateGuideCustomRepository {

    boolean existsByOrderAndIsDeletedFalse(int order);

    boolean existsByIdNotAndOrderAndIsDeletedFalse(Long guide, int order);

    Optional<Guide> findByIdAndIsDeletedFalse(Long id);
}
