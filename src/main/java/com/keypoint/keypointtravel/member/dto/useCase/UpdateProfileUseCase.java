package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.UpdatePasswordRequest;
import com.keypoint.keypointtravel.member.dto.request.UpdateProfileRequest;
import com.keypoint.keypointtravel.member.dto.useCase.UpdatePasswordUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileUseCase {

    private Long memberId;

    private String name;

    private MultipartFile profileImage;

    public static UpdatePasswordUseCase from(Long memberId, UpdateProfileRequest request, MultipartFile profileImage) {
        return new UpdatePasswordUseCase(
                memberId,
                request.getName(),
                profileImage);
    }
}
