package com.keypoint.keypointtravel.theme.dto.useCase;

import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import com.keypoint.keypointtravel.theme.entity.ThemeColor;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateThemeUseCase {

    private String name;
    private String color;
    private List<String> chartColors;

    public static CreateThemeUseCase of(
        ThemeRequest request
    ){
        return new CreateThemeUseCase(
            request.getName(),
            request.getColor(),
            request.getChartColors()
        );
    }

    public Theme toThemeEntity(CreateThemeUseCase useCase) {
        List<ThemeColor> themeColors = useCase.getChartColors().stream()
            .map(ThemeColor::new)
            .collect(Collectors.toList());

        return Theme.builder()
            .name(useCase.getName())
            .color(useCase.getColor())
            .chartColors(themeColors)
            .build();
    }

    public PaidTheme toPaidThemeEntity(CreateThemeUseCase useCase){
        List<ThemeColor> themeColors = useCase.getChartColors().stream()
            .map(ThemeColor::new)
            .collect(Collectors.toList());

        return PaidTheme.builder()
            .name(useCase.getName())
            .color(useCase.getColor())
            .chartColors(themeColors)
            .build();
    }

}
