package com.keypoint.keypointtravel.theme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThemeRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    private String name;

    @NotEmpty(message = "테마를 입력해주세요.")
    @NotBlank(message = "테마를 입력해주세요.")
    private String color;

    @NotNull(message = "chartColors cannot be null.")
    private List<String> chartColors;
}
