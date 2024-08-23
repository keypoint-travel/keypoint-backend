package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.entity.PaymentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMemberRepository extends JpaRepository<PaymentMember, Long> {

}
