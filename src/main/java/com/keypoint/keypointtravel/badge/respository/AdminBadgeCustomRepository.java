package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;

public interface AdminBadgeCustomRepository {

    void updateBadge(UpdateBadgeUseCase useCase);
}
