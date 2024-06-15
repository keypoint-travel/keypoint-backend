package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.LoginRequest;
import lombok.Getter;

@Getter
public class LoginUseCase {

  private String email;
  private String password;

  public LoginUseCase(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public static LoginUseCase from(LoginRequest request) {
    return new LoginUseCase(request.getEmail(), request.getPassword());
  }
}
