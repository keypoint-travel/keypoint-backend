package com.keypoint.keypointtravel.receipt.repository;

import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.receipt.entity.Receipt;

@Repository
public class ReceiptRepository extends JpaRepository<Receipt, Long>{
    
}
