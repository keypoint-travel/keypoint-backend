package com.keypoint.keypointtravel.requestPush.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteRequestAlarmUseCase {

    private Long[] requestIds;

    public static DeleteRequestAlarmUseCase from(Long[] requestIds) {
        return new DeleteRequestAlarmUseCase(requestIds);
    }
}
