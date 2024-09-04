package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long>,
    PaymentItemCustomRepository {

    @Query("SELECT pi FROM PaymentItem pi WHERE pi.receipt.id = :receiptId ORDER BY pi.id")
    List<PaymentItem> findByReceiptId(@Param("receiptId") Long receiptId);
}
