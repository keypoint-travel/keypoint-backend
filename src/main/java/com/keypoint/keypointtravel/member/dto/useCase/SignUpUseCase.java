package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import lombok.Getter;

@Getter
public class SignUpUseCase {

  private String email;
  private String password;

  public SignUpUseCase(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public static SignUpUseCase from(SignUpRequest request) {
    return new SignUpUseCase(request.getEmail(), request.getPassword());
  }
}
