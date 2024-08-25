package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.entity.Badge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long>, AdminBadgeCustomRepository {

    boolean existsByNameAndIsDeletedFalse(String name);

    boolean existsByOrderAndIsDeletedFalse(int order);

    boolean existsByIdNotAndOrderAndIsDeletedFalse(Long guide, int order);

    Optional<Badge> findByIdAndIsDeletedFalse(Long id);
}
