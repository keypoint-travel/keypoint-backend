package com.keypoint.keypointtravel.inquiry.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inquiry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String inquiryTitle;

    @Column(nullable = false)
    private boolean isReplied;

    @Column(nullable = false)
    private boolean isDeleted;

    public Inquiry(Member member, String title) {
        this.member = member;
        this.inquiryTitle = title.length() > 10 ? title.substring(0, 10) + "..." : title;
        this.isDeleted = false;
        isReplied = false;
    }

    public void updateIsReplied(boolean isReplied) {
        this.isReplied = isReplied;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
