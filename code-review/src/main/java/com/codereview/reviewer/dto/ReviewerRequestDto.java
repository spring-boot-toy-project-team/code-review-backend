package com.codereview.reviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public class ReviewerRequestDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateReviewerDto {
    private Long memberId;
    private Set<String> skills;
    private String introduction;
    private Set<String> position;
    private String career;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateReviewerDto {
    private Long memberId;
    private Long reviewerId;
    private Set<String> skills;
    private String introduction;
    private Set<String> position;
    private String career;
  }
}
