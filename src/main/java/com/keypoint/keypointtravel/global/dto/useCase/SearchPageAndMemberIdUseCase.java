package com.keypoint.keypointtravel.global.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class SearchPageAndMemberIdUseCase {

    private String keyword;
    private Long memberId;
    private String sortBy;
    private String direction;
    private Pageable pageable;

    public static SearchPageAndMemberIdUseCase of(
        String keyword,
        Long memberId,
        String sortBy,
        String direction,
        Pageable pageable
    ) {
        return new SearchPageAndMemberIdUseCase(keyword, memberId, sortBy, direction, pageable);
    }
}

