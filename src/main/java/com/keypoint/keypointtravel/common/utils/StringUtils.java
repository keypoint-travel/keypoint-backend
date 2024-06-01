package com.keypoint.keypointtravel.common.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

  public static Float convertToFloat(String str) {
    return str == null ? null : Float.parseFloat(str);
  }

  public static Integer convertToInteger(String str) {
    return str == null ? null : Integer.parseInt(str);
  }
}
