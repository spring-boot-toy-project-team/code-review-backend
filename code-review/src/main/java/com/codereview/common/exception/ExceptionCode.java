package com.codereview.common.exception;

import lombok.Getter;

public enum ExceptionCode {
  MEMBER_NOT_FOUND(404, "Member not found"),
  MEMBER_ALREADY_EXISTS(409, "Member already exists"),
  FIELD_ERROR(400, "Field Error"),
  CONSTRAINT_VIOLATION_ERROR(400, "Constraint Violation Error"),
  NOT_IMPLEMENTATION(501, "Not Implementation"),
  INVALID_MEMBER_STATUS(400, "Invalid member status"),
  ROLE_IS_NOT_EXISTS(403, "Role is not exists"),
  MEMBER_INFO_INCORRECT(404, "Member Info Incorrect"),
  PASSWORD_INCORRECT(404, "Password Incorrect"),
  TOKEN_IS_INVALID(401, "Token is invalid"),
  REFRESH_TOKEN_IS_EXPIRED(403, "Refresh Token is expired"),
  COOKIE_IS_NOT_EXISTS(404, "Cookie is not exists"),
  AUTHORIZATION_IS_NOT_FOUND(403, "Authorization header not found"),
  BOARD_NOT_FOUND(404, "Board not found");

  @Getter
  private int status;

  @Getter
  private String message;

  ExceptionCode(int code, String message) {
    this.status = code;
    this.message = message;
  }
}
