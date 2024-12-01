package com.keypoint.keypointtravel.requestPush.dto.response;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestPushAlarmResponse {

    private Long requestId;
    private String roleType;
    private LanguageCode languageCode;
    private String title;
    private String content;
    private LocalDateTime reservationAt;
    private LocalDateTime sendAt;

    public RequestPushAlarmResponse(
        Long requestId,
        RoleType roleType,
        LanguageCode languageCode,
        String title,
        String content,
        LocalDateTime reservationAt
    ) {
        this.requestId = requestId;
        this.roleType = roleType == null ? null : roleType.getDescription();
        this.languageCode = languageCode;
        this.title = title;
        this.content = content;
        this.reservationAt = reservationAt;
        if (reservationAt.isBefore(LocalDateTime.now())) {
            this.sendAt = reservationAt;
        }
    }
}
