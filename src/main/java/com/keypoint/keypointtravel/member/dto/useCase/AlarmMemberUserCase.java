package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmMemberUserCase {

    private Long memberId;
    private String name;
    private LanguageCode language;
    private boolean pushNotificationEnabled;
}
