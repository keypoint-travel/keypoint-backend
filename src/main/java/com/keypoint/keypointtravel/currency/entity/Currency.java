package com.keypoint.keypointtravel.currency.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "currency")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency {

    @Id
    @Column(name = "id")
    private String cur_unit;

    @Column(nullable = false)
    private String cur_name;

    @Column(nullable = false)
    private Double exchange_rate;

    @Column(nullable = false)
    private String date;

    public Currency(String cur_unit, String cur_name, Double exchange_rate, String date) {
        this.cur_unit = cur_unit;
        this.cur_name = cur_name;
        this.exchange_rate = exchange_rate;
        this.date = date;
    }
}
