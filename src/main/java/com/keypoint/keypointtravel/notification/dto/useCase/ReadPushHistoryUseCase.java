package com.keypoint.keypointtravel.notification.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class ReadPushHistoryUseCase {

    private Long memberId;
    private Pageable pageable;

    public static ReadPushHistoryUseCase of(Long memberId, Pageable pageable) {
        return new ReadPushHistoryUseCase(memberId, pageable);
    }
}
