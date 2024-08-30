package com.keypoint.keypointtravel.notification.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushHistoryIdUseCase {

    private Long memberId;
    private Long historyId;

    public static PushHistoryIdUseCase of(
        Long memberId,
        Long historyId
    ) {
        return new PushHistoryIdUseCase(memberId, historyId);
    }
}
