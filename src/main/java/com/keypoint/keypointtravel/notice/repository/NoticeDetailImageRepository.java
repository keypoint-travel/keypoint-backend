package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.notice.entity.NoticeDetailImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDetailImageRepository extends JpaRepository<NoticeDetailImage, Long>, NoticeCustomRepository{
}
