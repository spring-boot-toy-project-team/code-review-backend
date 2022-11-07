package com.codereview.common.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TokenResponseDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Token {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiredTime;
    private Long refreshTokenExpiredTime;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ReIssueToken {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiredTime;
  }
}
