package com.codereview.util;

import java.time.LocalDateTime;

public class PathMaker {
  public static String makePathFromNow() {
    LocalDateTime time = LocalDateTime.now();
    return String.format("%d/%d/%d/%d",
      time.getYear(),
      time.getMonthValue(),
      time.getDayOfMonth(),
      time.getHour());
  }
}
