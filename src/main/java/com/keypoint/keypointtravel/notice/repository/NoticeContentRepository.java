package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeContentRepository
    extends JpaRepository<NoticeContent, Long>, NoticeCustomRepository {

}
