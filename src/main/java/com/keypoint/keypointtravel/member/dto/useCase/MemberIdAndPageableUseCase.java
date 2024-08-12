package com.keypoint.keypointtravel.member.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class MemberIdAndPageableUseCase {

    private Long memberId;

    private Pageable pageable;

    public static MemberIdAndPageableUseCase of(Long id, Pageable pageable) {
        return new MemberIdAndPageableUseCase(id, pageable);
    }
}