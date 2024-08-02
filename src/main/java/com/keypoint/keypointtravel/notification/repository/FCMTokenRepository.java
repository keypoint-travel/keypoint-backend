package com.keypoint.keypointtravel.notification.repository;

import com.keypoint.keypointtravel.notification.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

}
