package com.codereview.reviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public class ReviewerResponseDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ReviewerInfoDto {
    private Long reviewerId;
    private Set<String> skills;
    private String introduction;
    private String position;
    private String career;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ReviewerShortInfoDto {
    private Long reviewerId;
    private Set<String> skills;
    private String position;
    private String career;
  }
}
