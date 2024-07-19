package com.keypoint.keypointtravel.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<CommonMemberDTO> findByEmail(String email);
}
