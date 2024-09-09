package com.keypoint.keypointtravel.theme.dto.useCase;

import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import java.util.List;
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

    public Theme toThemeEntity(CreateThemeUseCase useCase){
        return new Theme(
            useCase.getName(),
            useCase.getColor(),
            useCase.getChartColors()
        );
    }

    public PaidTheme toPaidThemeEntity(CreateThemeUseCase useCase){
        return new PaidTheme(
            useCase.getName(),
            useCase.getColor(),
            useCase.getChartColors()
        );
    }

}
