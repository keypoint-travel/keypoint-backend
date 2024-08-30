package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class PushNotificationEvent {

    private PushNotificationType pushNotificationType; // 푸시 알림 타입

    private List<Long> memberIds; // 푸시 알림을 보낼 사용자 idc

    public abstract Object getAdditionalData();

    public void clearMemberIds() {
        this.memberIds = new ArrayList<>();
    }
}
