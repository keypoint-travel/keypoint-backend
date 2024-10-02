package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidThemeRepository extends
    JpaRepository<PaidTheme, Long>, UpdateThemeCustomRepository, DeleteThemeCustomRepository, MemberThemeCustomRepository  {
    Optional<PaidTheme> findByIdAndIsDeletedFalse(Long themeId);

}
