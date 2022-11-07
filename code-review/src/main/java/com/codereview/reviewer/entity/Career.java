package com.codereview.reviewer.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

public enum Career {
  JUNIOR("0 ~ 3년차"),
  MID_LEVEL("3 ~ 6년차"),
  SENIOR("6년차 이상");

  String description;

  Career(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
