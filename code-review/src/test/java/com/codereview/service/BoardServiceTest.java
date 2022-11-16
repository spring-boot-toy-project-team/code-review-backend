package com.codereview.service;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.board.service.BoardService;
import com.codereview.board.service.BoardTagService;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.BoardTagStubData;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
  @Spy
  @InjectMocks
  private BoardService boardService;
  @Mock
  private BoardRepository boardRepository;
  @Mock
  private BoardTagService boardTagService;

  @Test
  @DisplayName("게시판 저장 테스트")
  public void getBoardsTest() throws Exception {
    // given
    Board board = BoardStubData.board();
    BoardTag boardTag = BoardTagStubData.boardTag();
    board.setBoardTags(List.of(boardTag));

    given(boardRepository.save(Mockito.any(Board.class))).willReturn(board);

    // when
    Board savedBoard = boardService.createBoard(board);

    // then
    Assertions.assertThat(board).isEqualTo(savedBoard);
  }
}
