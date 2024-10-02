package com.keypoint.keypointtravel.theme.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThemeResponse {
    private Long themeId;
    private String name;
    private String color;
    private List<String> chartColors;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public ThemeResponse(Long themeId, String name, String color, List<String> chartColors,
        LocalDateTime createAt, LocalDateTime modifyAt) {
        this.themeId = themeId;
        this.name = name;
        this.color = color;
        this.chartColors = chartColors;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }

    public void setChartColors(List<String> chartColors) {
        this.chartColors = chartColors;
    }


}
