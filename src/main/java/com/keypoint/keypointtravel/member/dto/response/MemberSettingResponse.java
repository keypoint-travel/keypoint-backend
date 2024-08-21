package com.keypoint.keypointtravel.member.dto.response;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSettingResponse {

    private LanguageCode language;
    private boolean pushNotificationEnabled;
}
