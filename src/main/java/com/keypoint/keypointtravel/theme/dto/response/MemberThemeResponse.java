package com.keypoint.keypointtravel.theme.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberThemeResponse {

    private String color;
    private String name;
    private List<String> chartColors;

    public MemberThemeResponse withChartColors(List<String> chartColors) {
        this.chartColors = chartColors;
        return this;
    }

    public MemberThemeResponse(String color, String name, List<String> chartColors) {
        this.color = color;
        this.name = name;
        this.chartColors = chartColors;
    }
}
