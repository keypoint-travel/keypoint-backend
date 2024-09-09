package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends
    JpaRepository<Theme, Long>, ThemeCustomRepository{

}
