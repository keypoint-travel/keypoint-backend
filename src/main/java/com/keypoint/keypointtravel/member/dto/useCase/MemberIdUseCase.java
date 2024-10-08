package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberIdUseCase {

    private Long memberId;
    private String email;

    public static MemberIdUseCase from(CustomUserDetails userDetails) {
        return new MemberIdUseCase(
            userDetails.getId(),
            userDetails.getEmail()
        );
    }

    public static MemberIdUseCase from(Long memberId) {
        return new MemberIdUseCase(memberId, null);
    }
}
