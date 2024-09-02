package com.keypoint.keypointtravel.guide.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "guide")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guide extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id")
    private Long id;

    @Column(nullable = false)
    private Long thumbnailImageId;

    @Column(nullable = false, name = "order_number")
    private int order;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "guide", orphanRemoval = true)
    private List<GuideTranslation> guideTranslations = new ArrayList<>();

    public Guide(
        Long thumbnailImageId,
        int order
    ) {
        this.thumbnailImageId = thumbnailImageId;
        this.order = order;
        this.isDeleted = false;
    }
}
