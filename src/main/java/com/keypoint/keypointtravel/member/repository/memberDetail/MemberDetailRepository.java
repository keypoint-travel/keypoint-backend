package com.keypoint.keypointtravel.member.repository.memberDetail;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
        + "SET md.representativeBadge = :representativeBadge "
        + "WHERE md.member.id = :memberId")
    int updateRepresentativeBadge(
        @Param("memberId") Long memberId,
        @Param("representativeBadge") Badge representativeBadge
    );

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.theme = :theme "
        + "WHERE md.member.id = :memberId")
    int updateTheme(
        @Param("memberId") Long memberId,
        @Param("theme") Theme theme
    );

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.paidTheme = :paidTheme "
        + "WHERE md.member.id = :memberId")
    int updatePaidTheme(
        @Param("memberId") Long memberId,
        @Param("paidTheme")PaidTheme paidTheme
    );

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md SET md.theme = null, md.paidTheme = null WHERE md.member.id = :memberId")
    int clearThemes(@Param("memberId") Long memberId);

    @Query("SELECT md.profileImageId FROM MemberDetail md WHERE md.member.id = :memberId")
    Optional<Long> findProfileImageIdByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT md.language FROM MemberDetail md WHERE md.member.id = :memberId")
    LanguageCode findLanguageCodeByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT md FROM MemberDetail md WHERE md.member.id = :memberId")
    MemberDetail findByMemberId(@Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.recentRegisterReceiptAt = :recentRegisterReceiptAt "
        + "WHERE md.member.id = :memberId")
    int updateRecentRegisterReceiptAt(
        @Param("memberId") Long memberId,
        @Param("recentRegisterReceiptAt") LocalDateTime recentRegisterReceiptAt
    );
}
