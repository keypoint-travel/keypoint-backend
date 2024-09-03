package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long>, ReceiptCustomRepository {

}
