package com.keypoint.keypointtravel.place.entity;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false, name = "city_en")
    private String cityEN;

    @Column(nullable = false, name = "city_ko")
    private String cityKO;

    @Column(nullable = false, name = "city_jp")
    private String cityJP;

    @Comment("경도")
    @Column(nullable = false)
    private Double longitude;

    @Comment("위도")
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;
}
