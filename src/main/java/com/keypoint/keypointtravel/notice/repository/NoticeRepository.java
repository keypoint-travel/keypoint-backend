package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NoticeRepository extends
    JpaRepository<Notice, Long>, NoticeCustomRepository {

}
