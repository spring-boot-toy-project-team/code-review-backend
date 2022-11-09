package com.codereview.board.mapper;

import com.codereview.board.dto.BoardRequestDto;
import com.codereview.board.dto.BoardResponseDto;
import com.codereview.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
  BoardResponseDto.BoardDto boardToBoardDto(Board board);
  List<BoardResponseDto.BoardDto> boardListToBoardDtoList(List<Board> boardList);

  BoardResponseDto.BoardInfoDto boardToBoardInfoDto(Board board);

  Board createBoardDtoToBoard(BoardRequestDto.CreateBoardDto createBoardDto);

  Board updateBoardToBoard(BoardRequestDto.UpdateBoardDto updateBoardDto);
}
