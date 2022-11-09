package com.codereview.stub;

import com.codereview.board.entity.Board;
import com.codereview.helper.RestPage;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class BoardStubData {

  public static Board board() {
    return Board.builder()
      .boardId(1L)
      .contents("내용")
      .title("제목")
      .build();
  }

  public static RestPage<Board> boardPageRequest(int page, int size) {
    return new RestPage<>(new PageImpl<>(List.of(
      board()
    ), PageRequest.of(page, size, Sort.by("boardId").descending()), 1));
  }
}
