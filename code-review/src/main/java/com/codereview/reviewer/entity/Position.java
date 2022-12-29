package com.codereview.reviewer.entity;

public enum Position {
  BACK_END("백엔드 개발"),
  FRONT_END("프론트 개발"),
  FULL_STACK("풀스택 개발"),
  DBA("DBA"),
  DEVOPS("DevOps"),
  GAME("게임 개발"),
  SECURE("보안 담당");

  String description;

  Position(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
