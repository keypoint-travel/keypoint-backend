package com.keypoint.keypointtravel.member.repository.memberDetail;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE MemberDetail md SET md.language = :language WHERE md.member.id = :memberId")
    int updateLanguage(
        @Param("memberId") Long memberId,
        @Param("language") LanguageCode language);

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.name = :name, md.profileImageId = :profileImageId "
        + "WHERE md.member.id = :memberId")
    int updateMemberProfile(
        @Param("memberId") Long memberId,
        @Param("name") String name,
        @Param("profileImageId") Long profileImageId
    );

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.representativeBadge = :representativeBadge "
        + "WHERE md.member.id = :memberId")
    int updateRepresentativeBadge(
        @Param("memberId") Long memberId,
        @Param("representativeBadge") Badge representativeBadge
    );

    @Query("SELECT md.profileImageId FROM MemberDetail md WHERE md.member.id = :memberId")
    Optional<Long> findProfileImageIdByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT md.language FROM MemberDetail md WHERE md.member.id = :memberId")
    LanguageCode findLanguageCodeByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT md FROM MemberDetail md WHERE md.member.id = :memberId")
    MemberDetail findByMemberId(@Param("memberId") Long memberId);
}
