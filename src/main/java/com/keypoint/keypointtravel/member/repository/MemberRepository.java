package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByEmail(String email);
}
