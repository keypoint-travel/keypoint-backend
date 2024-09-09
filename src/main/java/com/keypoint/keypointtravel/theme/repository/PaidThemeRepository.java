package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidThemeRepository extends
    JpaRepository<PaidTheme, Long>, ThemeCustomRepository{

}
