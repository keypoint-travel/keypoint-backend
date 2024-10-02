package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;

public interface MemberThemeCustomRepository {
    MemberThemeResponse findThemeByUserId(Long memberId);

}
