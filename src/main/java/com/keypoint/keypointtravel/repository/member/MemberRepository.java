package com.keypoint.keypointtravel.repository.member;

import com.keypoint.keypointtravel.entity.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByEmail(String email);
}
