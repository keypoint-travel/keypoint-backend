package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.UpdatePasswordRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePasswordUseCase {

    private String email;

    private String newPassword;

    public static UpdatePasswordUseCase from(UpdatePasswordRequest request) {
        return new UpdatePasswordUseCase(
            request.getEmail(),
            request.getNewPassword()
        );
    }
}
