package com.keypoint.keypointtravel.auth.dto.useCase;

import lombok.Getter;

@Getter
public class ReissueUseCase {

  private String accessToken;
  private String refreshToken;

  public ReissueUseCase(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public static ReissueUseCase of(String accessToken, String refreshToken) {
    return new ReissueUseCase(accessToken, refreshToken);
  }
}
