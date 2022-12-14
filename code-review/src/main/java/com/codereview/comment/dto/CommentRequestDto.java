package com.codereview.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CommentRequestDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class CommentDto {
    private String contents;
    private long boardId;
    private long memberId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class updateCommentDto {
    private String contents;
    private long boardId;
    private long memberId;
    private long commentId;
  }
}
