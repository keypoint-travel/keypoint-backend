package com.keypoint.keypointtravel.global.dto.useCase;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageAndMemberIdUseCase {

    private Long memberId;
    private String sortBy;
    private String direction;
    private Pageable pageable;

    public static PageAndMemberIdUseCase of(
        Long memberId,
        String sortBy,
        String direction,
        Pageable pageable
    ) {
        return new PageAndMemberIdUseCase(memberId, sortBy, direction, pageable);
    }
}
