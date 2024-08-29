package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationHistoryRepository extends
    JpaRepository<PushNotificationHistory, Long>, PushNotificationHistoryCustomRepository {

    @Transactional
    @Modifying
    @Query("UPDATE PushNotificationHistory pnh "
        + "SET pnh.isRead = true "
        + "WHERE pnh.id = :id and pnh.member.id = :memberId")
    int updateIsReadTrueByHistoryId(
        @Param("id") Long id,
        @Param("memberId") Long memberId
    );
}
