package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE MemberDetail md SET md.language = :language WHERE md.member.id = :memberId")
    int updateLanguage(
        @Param("memberId") Long memberId,
        @Param("language") LanguageCode language);

    @Modifying
    @Query("UPDATE MemberDetail md "
        + "SET md.name = :name, md.profileImageId = :profileImageId "
        + "WHERE md.member.id = :memberId")
    int updateMemberProfile(
        @Param("memberId") Long memberId,
        @Param("name") String name,
        @Param("profileImageId") Long profileImageId
    );

    @Query("SELECT md.profileImageId FROM MemberDetail md WHERE md.member.id = :memberId")
    Optional<Long> findProfileImageIdByMemberId(@Param("memberId") Long memberId);
}
