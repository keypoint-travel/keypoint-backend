package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long>, ReceiptCustomRepository {
    @Query("SELECT r.receiptRegistrationType FROM Receipt r WHERE r.id = :receiptId")
    Optional<ReceiptRegistrationType> findReceiptRegistrationTypeByReceiptId(@Param("receiptId") Long receiptId);
}
