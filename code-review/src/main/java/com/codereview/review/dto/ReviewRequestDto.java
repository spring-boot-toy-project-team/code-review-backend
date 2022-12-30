package com.codereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class ReviewRequestDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CreateReviewDto {
    private Long reviewerId;
    private Long memberId;
    @NotBlank
    private String contents;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateReviewDto {
    private Long reviewId;
    private Long memberId;
    @NotBlank
    private String contents;
  }
}
