package com.codereview.review.dto;

import com.codereview.member.dto.MemberResponseDto.MemberInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReviewResponseDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ReviewInfoDto {
    private Long reviewId;
    private MemberInfoDto reviewerInfo;
    private String contents;
  }
}
