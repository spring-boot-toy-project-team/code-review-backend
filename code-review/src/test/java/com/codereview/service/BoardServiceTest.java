package com.codereview.service;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.board.service.BoardService;
import com.codereview.board.service.BoardTagService;
import com.codereview.common.helper.RestPage;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.BoardTagStubData;
import com.codereview.tag.service.TagService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
  @Mock
  private TagService tagService;

  @Test
  @DisplayName("게시글 저장 테스트")
  public void createBoardTest() throws Exception {
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

  @Test
  @DisplayName("게시판 조회 테스트")
  public void getBoardsTest() throws Exception {
    // given
    int page = 1, size = 10;
    Pageable pageable = PageRequest.of(page, size, Sort.by("boardId").descending());
    RestPage<Board> boardRestPage = BoardStubData.BoardRestPage(page, size);

    given(boardRepository.findAll(Mockito.any(Pageable.class))).willReturn(boardRestPage);

    // when
    RestPage<Board> findBoardRestPage = boardService.getBoards(pageable);

    // then
    Assertions.assertThat(boardRestPage.getContent().size()).isEqualTo(findBoardRestPage.getContent().size());
  }

  @Test
  @DisplayName("내 게시글 조회")
  public void getMyBoardsTest() throws Exception {
    // given
    long memberId = 1L;
    int page = 1, size = 10;
    Pageable pageable = PageRequest.of(page, size, Sort.by("boardId").descending());
    RestPage<Board> boardRestPage = BoardStubData.BoardRestPage(page, size);

    given(boardRepository.findAllByMemberId(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(boardRestPage);

    // when
    Page<Board> findBoardRestPage = boardService.getMyBoards(memberId, pageable);

    // then
    Assertions.assertThat(boardRestPage.getContent().size()).isEqualTo(findBoardRestPage.getContent().size());
  }
}
