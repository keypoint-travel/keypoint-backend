package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeContentRepository
    extends JpaRepository<NoticeContent, Long>, NoticeCustomRepository {
    Optional<NoticeContent> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndLanguageCodeAndIsDeletedFalse(Long noticeContentId,
        LanguageCode languageCode);
}
