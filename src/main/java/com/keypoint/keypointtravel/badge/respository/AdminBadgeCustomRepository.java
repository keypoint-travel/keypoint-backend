package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.request.BadgeIdRequest;
import com.keypoint.keypointtravel.badge.dto.response.BadgeInAdminResponse;
import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import org.springframework.data.domain.Page;

public interface AdminBadgeCustomRepository {

    void updateBadge(UpdateBadgeUseCase useCase);

    void deleteGuides(DeleteBadgeUseCase useCase);

    BadgeInAdminResponse findBadgeById(BadgeIdRequest useCase);

    Page<BadgeInAdminResponse> findBadges(PageUseCase useCase);
}
