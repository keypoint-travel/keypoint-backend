package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityErrorCode implements ErrorCode {
  ACCESS_DENIED("001_ACCESS_DENIED", "접근 권한이 존재하지 않습니다.");

  private final String code;
  private final String msg;
}
