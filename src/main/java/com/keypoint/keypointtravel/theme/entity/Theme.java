package com.keypoint.keypointtravel.theme.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThemeColor> chartColors = new ArrayList<>();

    @Column
    private boolean isDeleted;

    @Builder
    public Theme(String name, String color, List<ThemeColor> chartColors) {
        this.name = name;
        this.color = color;
        this.chartColors = chartColors;
        this.isDeleted = false;
    }

    public Theme() {
        this.isDeleted = false;
    }

    public void updateChartColors(List<ThemeColor> chartColors) {
        this.chartColors.clear();
        this.chartColors.addAll(chartColors);
    }


}
