package com.codereview.stub;

import com.codereview.board.dto.board.BoardRequestDto;
import com.codereview.board.dto.board.BoardResponseDto;
import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.helper.RestPage;
import com.codereview.member.entity.Member;
import com.codereview.tag.entity.Tag;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardStubData {
  private final static Member member = MemberStubData.member();

  public static Board board() {
    return Board.builder()
      .boardId(1L)
      .contents("내용")
      .title("제목")
      .member(member)
      .build();
  }

  public static Board board(long boardId, String contents, String title) {
    return Board.builder()
      .boardId(boardId)
      .contents(contents)
      .title(title)
      .member(member)
      .build();
  }

  public static RestPage<Board> boardPageRequest(int page, int size) {
    return new RestPage<>(new PageImpl<>(List.of(
      board(1L, "내용1", "제목1"),
      board(2L, "내용2", "제목3"),
      board(3L, "내용3", "제목3")
    ), PageRequest.of(page, size, Sort.by("boardId").descending()), 1));
  }

  public static BoardRequestDto.CreateBoardDto CreateBoardDto() {
    return BoardRequestDto.CreateBoardDto.builder()
      .contents("내용")
      .title("제목")
      .tagList(Set.of("A", "B"))
      .memberId(1L)
      .build();
  }

  public static Board CreateBoardDtoToBoardDto(BoardRequestDto.CreateBoardDto createBoardDto) {
    return Board.builder()
      .title(createBoardDto.getTitle())
      .contents(createBoardDto.getContents())
      .member(member)
      .boardTags(createBoardDto.getTagList().stream().map(name -> {
          Tag tag = Tag.builder().name(name).build();
          return BoardTag.builder().tag(tag).build();
        })
        .collect(Collectors.toList())
      )
      .boardId(1L)
      .build();
  }

  public static BoardResponseDto.BoardInfoDto BoardToBoardInfoDto(Board board) {
    return BoardResponseDto.BoardInfoDto.builder()
      .boardId(board().getBoardId())
      .contents(board().getContents())
      .createdAt(LocalDateTime.now())
      .modifiedAt(LocalDateTime.now())
      .title(board().getTitle())
      .tag(board.getBoardTags().stream().map(boardTag -> boardTag.getTag().getName())
        .collect(Collectors.toSet())
      )
      .build();
  }
}
