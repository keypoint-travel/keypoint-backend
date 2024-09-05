package com.keypoint.keypointtravel.premium.repository;

import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPremiumRepository extends JpaRepository<MemberPremium, Long> {

    Optional<MemberPremium> findByMemberId(Long memberId);
}
