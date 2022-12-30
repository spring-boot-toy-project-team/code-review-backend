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
  BOARD_NOT_FOUND(404, "Board not found"),
  TAG_NOT_FOUND(404, "Tag not found"),
  CODE_INCORRECT(404, "Code Incorrect"),
  EMAIL_INCORRECT(404, "Email Incorrect"),
  PHONE_ALREADY_EXISTS(409, "Phone already exists" ),
  PHONE_NOT_FOUND(404, "Phone not found" ),
  CODE_NOT_FOUND(404, "Code not found" ),
  FILE_UPLOAD_FAIL(500, "File upload fail"),
  INVALID_FILE_FORMAT(500, "Invalid file format"),
  IMG_NOT_FOUND(404, "Image not found"),
  REVIEWER_ALREADY_EXISTS(409, "Reviewer already exists"),
  REVIEWER_NOT_FOUND(404, "Reviewer not found"), FORBIDDEN(403, "Forbidden"),
  COMMENT_NOT_FOUND(404,"Comment not found"),
  REVIEW_NOT_FOUND(404, "Review Not Found");

  @Getter
  private int status;

  @Getter
  private String message;

  ExceptionCode(int code, String message) {
    this.status = code;
    this.message = message;
  }
}
