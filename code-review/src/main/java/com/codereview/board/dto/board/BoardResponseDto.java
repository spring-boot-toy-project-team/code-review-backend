package com.codereview.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private Set<String> tag;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  }
}
