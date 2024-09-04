package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.dto.PaymentMemberDTO;
import com.keypoint.keypointtravel.receipt.entity.PaymentMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMemberRepository extends JpaRepository<PaymentMember, Long> {

    @Query(
        "SELECT new com.keypoint.keypointtravel.receipt.dto.dto.PaymentMemberDTO(pm.id, pm.member.id) "
            + "FROM PaymentMember pm "
            + "WHERE pm.paymentItem.id = :paymentItemId"
    )
    List<PaymentMemberDTO> findByPaymentItemId(@Param("paymentItemId") Long paymentItemId);
}
