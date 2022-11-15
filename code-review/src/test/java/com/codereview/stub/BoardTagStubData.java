package com.codereview.stub;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.tag.entity.Tag;

import java.util.List;

public class BoardTagStubData {
  private static final Tag tag = TagStubData.tag();
  private static final Board board = BoardStubData.board();

  public static BoardTag boardTag() {
    return BoardTag.builder()
      .tag(tag)
      .boardTagId(1L)
      .board(board)
      .build();
  }

  public static List<BoardTag> boardTagList() {
    return List.of(boardTag());
  }
}
