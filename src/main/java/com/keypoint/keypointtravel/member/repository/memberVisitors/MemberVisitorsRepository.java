package com.keypoint.keypointtravel.member.repository.memberVisitors;

import com.keypoint.keypointtravel.member.entity.MemberVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberVisitorsRepository extends JpaRepository<MemberVisitors, Long>,
    MemberVisitorsCustomRepository {


}
