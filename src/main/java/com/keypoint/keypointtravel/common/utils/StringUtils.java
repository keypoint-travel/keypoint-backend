package com.keypoint.keypointtravel.common.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

  public static Float changeStringToFloat(String str) {
    return str == null ? null : Float.parseFloat(str);
  }
}
