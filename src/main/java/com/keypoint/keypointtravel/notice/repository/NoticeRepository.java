package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.notice.entity.Notice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends
    JpaRepository<Notice, Long>, NoticeCustomRepository {

    @Query("SELECT n.thumbnailImageId FROM Notice n WHERE n.id = :id")
    Optional<Long> findThumbnailImageIdById(@Param(value = "id") Long id);
}
