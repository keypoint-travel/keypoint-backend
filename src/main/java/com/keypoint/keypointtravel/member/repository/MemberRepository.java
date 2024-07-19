package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<CommonMemberDTO> findByEmail(String email);

    boolean existsByEmail(String email);
}
