package com.keypoint.keypointtravel.theme.dto.useCase;

import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateThemeUseCase {

    private Long themeId;
    private String name;
    private String color;
    private List<String> chartColors;

    public static UpdateThemeUseCase of(
        Long themeId,
        ThemeRequest request
    ) {
        return new UpdateThemeUseCase(
            themeId,
            request.getName(),
            request.getColor(),
            request.getChartColors()
        );
    }

}
