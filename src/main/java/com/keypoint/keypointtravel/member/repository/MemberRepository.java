package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<CommonMemberDTO> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.recentLoginAt = :recentLoginAt WHERE m.id = :id")
    int updateRecentLoginAtByMemberId(
        @Param("id") Long id,
        @Param("recentLoginAt") LocalDateTime recentLoginAt
    );
}
