package com.keypoint.keypointtravel.theme.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "theme")
public class Theme extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "theme_colors", joinColumns = @JoinColumn(name = "main_entity_id"))
    @Column(name = "color")
    private List<String> chartColors;

    @Column
    private boolean isDeleted;

    @Builder
    public Theme(String name, String color, List<String> chartColors) {
        this.name = name;
        this.color = color;
        this.chartColors = chartColors;
        this.isDeleted = false;
    }

    public Theme() {
        this.isDeleted = false;
    }

    public void updateChartColors(List<String> chartColors) {
        this.chartColors.clear();
        this.chartColors.addAll(chartColors);
    }


}
