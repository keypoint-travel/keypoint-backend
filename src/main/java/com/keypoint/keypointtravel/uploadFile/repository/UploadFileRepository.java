package com.keypoint.keypointtravel.uploadFile.repository;

import com.keypoint.keypointtravel.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

}
