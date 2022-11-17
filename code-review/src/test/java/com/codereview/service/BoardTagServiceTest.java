package com.codereview.service;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.board.repository.boardTags.BoardTagRepository;
import com.codereview.board.service.BoardTagService;
import com.codereview.stub.BoardStubData;
import com.codereview.stub.BoardTagStubData;
import com.codereview.tag.repository.TagRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardTagServiceTest {
  @Spy
  @InjectMocks
  private BoardTagService boardTagService;

  @Mock
  private TagService tagService;

  @Mock
  private TagRepository tagRepository;

  @Mock
  private BoardTagRepository boardTagRepository;

  @Mock
  private BoardRepository boardRepository;

  @Test
  @DisplayName("BoardTag 저장 테스트")
  public void createMultipleBoardTagTest() throws Exception {
    // given
    Board board = BoardStubData.board();
    BoardTag boardTag = BoardTagStubData.boardTag();
    List<BoardTag> boardTagList = List.of(boardTag);
    board.setBoardTags(boardTagList);

    given(boardTagRepository.findAllByBoardId(Mockito.anyLong())).willReturn(boardTagList);
    doNothing().when(boardTagService).createMultipleBoardTag(Mockito.any(Board.class));

    // when
    boardTagService.createMultipleBoardTag(board);

    // then
    List<BoardTag> findBoardTagList = boardTagRepository.findAllByBoardId(board.getBoardId());
    Assertions.assertThat(board.getBoardTags().size()).isEqualTo(findBoardTagList.size());
  }

  @Test
  @DisplayName("게시글 식별자로 게시글-태그 조회 테스트")
  public void findBoardTagsByBoardIdTest() throws Exception {
    // given
    long boardId = 1L;
    List<BoardTag> boardTagList = BoardTagStubData.boardTagList();

    given(boardTagRepository.findAllByBoardId(Mockito.anyLong())).willReturn(boardTagList);

    // when
    List<BoardTag> findBoardTagList = boardTagService.findBoardTagsByBoardId(boardId);

    // then
    Assertions.assertThat(boardTagList.size()).isEqualTo(findBoardTagList.size());
  }

  @Test
  @DisplayName("게시글-태그 식별자 삭제 테스트")
  public void deleteBoardTagTest() throws Exception {
    // given
    BoardTag boardTag = BoardTagStubData.boardTag();

    // when
    boardTagService.deleteBoardTag(boardTag);

    // then
    Mockito.verify(boardTagRepository).delete(boardTag);
  }

}
