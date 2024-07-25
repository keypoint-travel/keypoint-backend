package com.keypoint.keypointtravel.member.dto.response.memberProfile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAlarmResponse {

    private boolean pushNotificationEnabled;
    private boolean marketingNotificationEnabled;
}
