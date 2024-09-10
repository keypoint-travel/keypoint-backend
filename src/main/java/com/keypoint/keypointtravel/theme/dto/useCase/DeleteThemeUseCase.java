package com.keypoint.keypointtravel.theme.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class DeleteThemeUseCase {

    private Long[] themeIds;

    public static DeleteThemeUseCase from(Long[] themeIds) {
        return new DeleteThemeUseCase(themeIds);
    }
}
