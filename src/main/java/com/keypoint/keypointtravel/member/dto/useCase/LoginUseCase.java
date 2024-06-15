package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.LoginRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUseCase {

  private String email;
  private String password;

  public static LoginUseCase from(LoginRequest request) {
    return new LoginUseCase(request.getEmail(), request.getPassword());
  }
}
