package com.keypoint.keypointtravel.inquiry.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inquiry_detail_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryDetailImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_detail_id")
    private InquiryDetail inquiryDetail;

    @Column
    private Long inquiryImageId;

    public InquiryDetailImage(InquiryDetail inquiryDetail, Long inquiryImageId) {
        this.inquiryDetail = inquiryDetail;
        this.inquiryImageId = inquiryImageId;
    }
}
