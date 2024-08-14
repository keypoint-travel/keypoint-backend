package com.keypoint.keypointtravel.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "country")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(nullable = false, name = "country_en")
    private String countryEN;

    @Column(nullable = false, name = "country_ko")
    private String countryKO;

    @Column(nullable = false, name = "country_jp")
    private String countryJP;

    @Comment("국가 코드")
    @Column(nullable = false)
    private String iso2;
}
