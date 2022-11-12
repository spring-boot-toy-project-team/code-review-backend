package com.codereview.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

public class BoardRequestDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class CreateBoardDto {
    private String title;
    private String contents;
    private Set<String> tagList;
    private long memberId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class UpdateBoardDto {
    private String title;
    private String contents;
    private Set<String> tagList;
    private long memberId;
    private long boardId;
  }
}
