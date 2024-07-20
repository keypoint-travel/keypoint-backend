package com.keypoint.keypointtravel.member.repository;

import com.keypoint.keypointtravel.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

}
