package com.keypoint.keypointtravel.notification.repository.fcmToken;

import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.notification.dto.dto.CommonFCMTokenDTO;
import com.keypoint.keypointtravel.notification.entity.FCMToken;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long>,
    FCMTokenCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommonFCMTokenDTO> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE FCMToken f SET f.member = :member WHERE f.id = :id")
    int updateMember(
        @Param("id") Long id,
        @Param("member") Member member
    );

    @Query("SELECT f.token FROM FCMToken f WHERE f.member.id = :memberId")
    List<String> findTokenByMemberId(@Param("memberId") Long memberId);
}
