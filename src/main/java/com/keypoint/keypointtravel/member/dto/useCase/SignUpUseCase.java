package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpUseCase {

  private String email;
  private String password;

  public static SignUpUseCase from(SignUpRequest request) {
    return new SignUpUseCase(request.getEmail(), request.getPassword());
  }
}
