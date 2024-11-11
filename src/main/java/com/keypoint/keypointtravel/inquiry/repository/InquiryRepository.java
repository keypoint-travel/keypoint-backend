package com.keypoint.keypointtravel.inquiry.repository;

import com.keypoint.keypointtravel.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
