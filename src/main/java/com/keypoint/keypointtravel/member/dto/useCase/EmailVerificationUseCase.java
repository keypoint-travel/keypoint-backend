package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.EmailVerificationRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationUseCase {
    private String email;
    private String code;

  public static EmailVerificationUseCase from(EmailVerificationRequest request) {
    return new EmailVerificationUseCase(request.getEmail(), request.getCode());
  }
}
