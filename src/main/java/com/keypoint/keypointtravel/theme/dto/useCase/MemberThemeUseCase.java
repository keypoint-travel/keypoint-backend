package com.keypoint.keypointtravel.theme.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberThemeUseCase {

    private Long memberId;
    private Long themeId;
    private boolean isPaid;

    public MemberThemeUseCase(Long themeId) {
        this.themeId = themeId;
    }

    public static MemberThemeUseCase from(Long themeId) {
        return new MemberThemeUseCase(themeId);
    }

    public static MemberThemeUseCase of(Long memberId, Long themeId, boolean isPaid) {
        return new MemberThemeUseCase(memberId, themeId, isPaid);
    }

}
