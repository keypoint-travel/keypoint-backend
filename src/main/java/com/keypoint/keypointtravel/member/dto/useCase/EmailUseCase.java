package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.EmailRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailUseCase {
    private String email;

    public static EmailUseCase from(EmailRequest request) {
        return new EmailUseCase(request.getEmail());
    }
}
