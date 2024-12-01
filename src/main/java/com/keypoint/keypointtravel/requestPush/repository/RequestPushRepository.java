package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPushRepository extends
    JpaRepository<RequestAlarm, Long>,
    RequestPushCustomRepository {

    List<RequestAlarm> findAllByReservationAt(LocalDateTime reservationAt);
}
