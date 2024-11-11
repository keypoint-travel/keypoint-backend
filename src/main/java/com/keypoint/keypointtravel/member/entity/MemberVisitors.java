package com.keypoint.keypointtravel.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_visitors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberVisitors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_visitors_id")
    private Long id;

    private LocalDate date;

    private long visitors;

    public MemberVisitors(long visitors) {
        this.visitors = visitors;
        this.date = LocalDate.now();
    }

    public static MemberVisitors from(long visitors) {
        return new MemberVisitors(visitors);
    }
}
