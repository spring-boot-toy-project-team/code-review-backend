package com.codereview.board.mapper;

import com.codereview.board.dto.board.BoardRequestDto;
import com.codereview.board.dto.board.BoardResponseDto;
import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.member.entity.Member;
import com.codereview.tag.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
  BoardResponseDto.BoardDto boardToBoardDto(Board board);
  List<BoardResponseDto.BoardDto> boardListToBoardDtoList(List<Board> boardList);

  default BoardResponseDto.BoardInfoDto boardToBoardInfoDto(Board board) {
    return BoardResponseDto.BoardInfoDto.builder()
      .boardId(board.getBoardId())
      .title(board.getTitle())
      .contents(board.getContents())
      .createdAt(board.getCreatedAt())
      .modifiedAt(board.getModifiedAt())
      .tag(board.getBoardTags().stream()
        .map(boardTag -> boardTag.getTag().getName())
        .collect(Collectors.toSet())
      )
      .build();
  }

  default Board createBoardDtoToBoard(BoardRequestDto.CreateBoardDto createBoardDto) {
    Board board = Board.builder()
      .member(Member.builder().memberId(createBoardDto.getMemberId()).build())
      .title(createBoardDto.getTitle())
      .contents(createBoardDto.getContents())
      .build();
    List<BoardTag> boardTags = createBoardDto.getTagList().stream()
      .map(name -> {
        Tag tag = Tag.builder().name(name).build();
        return BoardTag.builder()
          .tag(tag)
          .board(board)
          .build();
      })
      .collect(Collectors.toList());
    board.setBoardTags(boardTags);
    return board;
  }

  default Board updateBoardToBoard(BoardRequestDto.UpdateBoardDto updateBoardDto) {
    Board board = Board.builder()
      .member(Member.builder().memberId(updateBoardDto.getMemberId()).build())
      .boardId(updateBoardDto.getBoardId())
      .title(updateBoardDto.getTitle())
      .contents(updateBoardDto.getContents())
      .build();
    List<BoardTag> boardTags = updateBoardDto.getTagList().stream()
      .map(name -> {
        Tag tag = Tag.builder().name(name).build();
        return BoardTag.builder()
          .tag(tag)
          .board(board)
          .build();
      })
      .collect(Collectors.toList());
    board.setBoardTags(boardTags);
    return board;
  }
}
