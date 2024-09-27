package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.entity.Theme;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends
    JpaRepository<Theme, Long>, ThemeCustomRepository, UpdateThemeCustomRepository, DeleteThemeCustomRepository, MemberThemeCustomRepository {
    Optional<Theme> findByIdAndIsDeletedFalse(Long themeId);

}
