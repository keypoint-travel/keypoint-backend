package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT uf.path "
        + "FROM UploadFile uf "
        + "INNER JOIN Badge b ON b.type = :type "
        + "WHERE uf.id = b.activeImageId")
    String findByActiveBadgeUrl(@Param(value = "type") BadgeType type);
}
