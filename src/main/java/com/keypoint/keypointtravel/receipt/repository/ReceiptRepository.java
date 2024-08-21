package com.keypoint.keypointtravel.receipt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.receipt.entity.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long>, CreateReceiptCustomRepository {

}
