package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RequestAlarmUseCase {

    private RoleType roleType;
    private LanguageCode languageCode;
    private String title;
    private String content;
    private LocalDateTime reservationAt;
}
