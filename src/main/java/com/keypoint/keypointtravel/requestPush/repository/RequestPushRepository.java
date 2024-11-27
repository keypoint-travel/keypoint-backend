package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.requestPush.entity.RequestPush;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPushRepository extends
    JpaRepository<RequestPush, Long>,
    RequestPushCustomRepository {

    List<RequestPush> findAllByReservationAt(LocalDateTime reservationAt);
}
