package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
    @Modifying
    @Query("UPDATE MemberDetail md SET md.language = :language WHERE md.memberId = :memberId")
    int updateLanguage(
            @Param("memberId") Long memberId,
            @Param("language") LanguageCode language);
}
