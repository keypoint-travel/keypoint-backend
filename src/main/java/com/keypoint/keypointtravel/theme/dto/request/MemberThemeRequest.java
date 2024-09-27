package com.keypoint.keypointtravel.theme.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberThemeRequest {
    private Long themeId;
    private boolean isPaid;

}
