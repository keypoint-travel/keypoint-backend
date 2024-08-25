package com.keypoint.keypointtravel.badge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;

    @Column(nullable = false)
    private Long activeImageId;

    @Column(nullable = false)
    private Long inactiveImageId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Column(nullable = false, unique = true, name = "order_number")
    private int order;

    public Badge(
        Long activeImageId,
        Long inactiveImageId,
        String name,
        int order
    ) {
        this.activeImageId = activeImageId;
        this.inactiveImageId = inactiveImageId;
        this.name = name;
        this.order = order;
        this.isDeleted = false;
    }
}
