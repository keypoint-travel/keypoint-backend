package com.keypoint.keypointtravel.notification.repository.notification;

import com.keypoint.keypointtravel.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>,
    NotificationCustomRepository {

}
