package com.keypoint.keypointtravel.theme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theme_color")
@Getter
@NoArgsConstructor
public class ThemeColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_theme_id")
    private PaidTheme paidTheme;

    @Column(name = "detail_color")
    private String color;

    @Builder
    public ThemeColor(Theme theme, PaidTheme paidTheme, String color) {
        this.theme = theme;
        this.paidTheme = paidTheme;
        this.color = color;
    }
    public ThemeColor(String color) {
        this.color = color;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setPaidTheme(PaidTheme paidTheme) {
        this.paidTheme = paidTheme;
    }
}

