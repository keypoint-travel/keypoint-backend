package com.keypoint.keypointtravel.inquiry.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "inquiry_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryDetail extends BaseEntity {

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

    @OneToMany(mappedBy = "inquiryDetail", orphanRemoval = true)
    private List<InquiryDetailImage> inquiryDetailImages = new ArrayList<>();

    public InquiryDetail(Inquiry inquiry, String inquiryContent, boolean isAdminReply) {
        this.inquiry = inquiry;
        this.inquiryContent = inquiryContent;
        this.isAdminReply = isAdminReply;
    }

    public void updateContent(String inquiryContent) {
        this.inquiryContent = inquiryContent;
    }
}
