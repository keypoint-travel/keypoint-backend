package com.keypoint.keypointtravel.member.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberIdUseCase {

    private Long memberId;

    public static MemberIdUseCase from(Long memberId) {
        return new MemberIdUseCase(memberId);
    }
}
