package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long>, AdminBadgeCustomRepository {

    boolean existsByName(String name);

    boolean existsByOrder(int order);

    boolean existsByIdNotAndOrder(Long guide, int order);
}
