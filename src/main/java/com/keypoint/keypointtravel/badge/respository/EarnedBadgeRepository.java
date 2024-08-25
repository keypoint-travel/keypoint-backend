package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.entity.EarnedBadge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EarnedBadgeRepository extends JpaRepository<EarnedBadge, Long> {

    @Query("SELECT eb "
        + "FROM EarnedBadge eb "
        + "WHERE eb.member.id = :memberId AND eb.badge.id = :badgeId")
    Optional<EarnedBadge> findByMemberIdAndBadgeId(
        @Param("memberId") Long memberId,
        @Param("badgeId") Long badgeId
    );
}
