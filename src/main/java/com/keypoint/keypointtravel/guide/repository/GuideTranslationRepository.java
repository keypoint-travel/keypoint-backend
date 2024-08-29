package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.entity.GuideTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideTranslationRepository extends JpaRepository<GuideTranslation, Long> {

    @Query("SELECT COUNT(g) > 0 "
        + "FROM GuideTranslation g "
        + "WHERE g.guide.id = :guideId AND g.languageCode = :languageCode AND g.isDeleted = false")
    boolean existsByGuideAndLanguage(@Param("guideId") Long guideId,
        @Param("languageCode") LanguageCode language);
}
