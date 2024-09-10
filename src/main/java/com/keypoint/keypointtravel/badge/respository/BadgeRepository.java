package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long>,
    AdminBadgeCustomRepository, MemberBadgeCustomRepository {

    boolean existsByNameAndIsDeletedFalse(String name);

    boolean existsByOrderAndIsDeletedFalse(int order);

    boolean existsByIdNotAndOrderAndIsDeletedFalse(Long guide, int order);

    Optional<Badge> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT b.type FROM Badge b WHERE b.isDeleted = false")
    Set<BadgeType> findBadgeTypeByIsDeletedFalse();

    @Query("SELECT b FROM Badge b WHERE b.type = :type")
    Badge findByBadgeType(@Param(value = "type") BadgeType type);
}
