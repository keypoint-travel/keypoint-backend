package com.keypoint.keypointtravel.inquiry.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inquiry_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    @Column(nullable = false)
    private String inquiryContent;

    @Column(nullable = false)
    private boolean isAdminReply;

    public InquiryDetail(Inquiry inquiry, String inquiryContent, boolean isAdminReply) {
        this.inquiry = inquiry;
        this.inquiryContent = inquiryContent;
        this.isAdminReply = isAdminReply;
    }
}
