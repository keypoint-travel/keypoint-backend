package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long> {

    @Query("SELECT pi FROM PaymentItem pi WHERE pi.receipt.id = :receiptId ORDER BY pi.id")
    List<PaymentItem> findByReceiptId(@Param("receiptId") Long receiptId);
}
