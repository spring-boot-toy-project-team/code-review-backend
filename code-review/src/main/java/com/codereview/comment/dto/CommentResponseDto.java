package com.codereview.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentResponseDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class CommentInfoDto {
    private Long commentId;
    private String nickName;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  }

}
