package com.keypoint.keypointtravel.global.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class PageUseCase {

    private String sortBy;
    private String direction;
    private Pageable pageable;

    public static PageUseCase of(
        String sortBy,
        String direction,
        Pageable pageable
    ) {
        return new PageUseCase(sortBy, direction, pageable);
    }
}
