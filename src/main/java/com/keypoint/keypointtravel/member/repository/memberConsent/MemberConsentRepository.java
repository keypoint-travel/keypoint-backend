package com.keypoint.keypointtravel.member.repository.memberConsent;

import com.keypoint.keypointtravel.member.entity.MemberConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberConsentRepository extends JpaRepository<MemberConsent, Long> {

}
