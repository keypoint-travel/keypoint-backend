package com.keypoint.keypointtravel.receipt.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentMember extends BaseEntity {

    @Id
    @Column(name = "payment_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_item_id")
    private PaymentItem paymentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public PaymentMember(PaymentItem paymentItem, Member member) {
        this.paymentItem = paymentItem;
        this.member = member;
    }

    public static PaymentMember of(PaymentItem paymentItem, Member member) {
        return new PaymentMember(paymentItem, member);
    }
}
