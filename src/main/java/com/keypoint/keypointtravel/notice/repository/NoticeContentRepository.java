package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeContentRepository
    extends JpaRepository<NoticeContent, Long>, NoticeCustomRepository {
    Optional<NoticeContent> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndLanguageCodeAndIsDeletedFalse(Long noticeContentId,
        LanguageCode languageCode);

    @Query("SELECT COUNT(nc) > 0 "
        + "FROM NoticeContent nc "
        + "WHERE nc.notice.id = :noticeId AND "
        + "nc.languageCode = :languageCode AND "
        + "nc.isDeleted = false AND "
        + "NOT nc.id = :noticeContentId")
    boolean existsByIdNotAndLanguageCodeAndIsDeletedFalse(
        @Param("noticeId") Long noticeId,
        @Param("noticeContentId") Long noticeContentId,
        @Param("languageCode") LanguageCode languageCode
    );
}
