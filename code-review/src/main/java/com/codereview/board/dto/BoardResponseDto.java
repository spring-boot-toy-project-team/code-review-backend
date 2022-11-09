package com.codereview.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponseDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BoardDto {
    private Long boardId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BoardInfoDto {
    private Long boardId;
    private String title;
    private String contents;
    private List<String> tag;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  }
}
