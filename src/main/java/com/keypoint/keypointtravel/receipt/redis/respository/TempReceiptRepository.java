package com.keypoint.keypointtravel.receipt.redis.respository;

import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempReceiptRepository extends
        CrudRepository<TempReceipt, String> {
}
