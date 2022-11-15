package com.codereview.board.controller;

import com.codereview.board.entity.Board;
import com.codereview.board.mapper.BoardMapper;
import com.codereview.board.service.BoardService;
import com.codereview.common.dto.response.MultiResponseWithPageInfoDto;
import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.helper.RestPage;
import com.codereview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;
import java.util.List;
import static com.codereview.board.dto.board.BoardRequestDto.*;

@Validated
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;
  private final BoardMapper mapper;

  /**
   * 게시판 리스트 조회
   */
  @GetMapping
  public ResponseEntity<MultiResponseWithPageInfoDto> getBoards(@Positive @PathParam("page") int page,
                                                                @Positive @PathParam("size") int size) {
    RestPage<Board> boardRestPage = boardService.getBoards(page -1, size);
    List<Board> boardList = boardRestPage.getContent();
    return new ResponseEntity<>(new MultiResponseWithPageInfoDto<>(
      mapper.boardListToBoardDtoList(boardList),
      boardRestPage),
      HttpStatus.OK);
  }

  /**
   * 게시판 검색
   */

  /**
   * 게시글 조회
   */
  @GetMapping("/{board-id}")
  public ResponseEntity<SingleResponseWithMessageDto> getBoard(@Positive @PathVariable("board-id") long boardId) {
    Board board = boardService.getBoard(boardId);
    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.boardToBoardInfoDto(board),
      "SUCCESS"),
      HttpStatus.OK);
  }

  /**
   * 게시글 작성
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @PostMapping
  public ResponseEntity<SingleResponseWithMessageDto> createBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  @Valid @RequestBody CreateBoardDto createBoardDto) {
    createBoardDto.setMemberId(customUserDetails.getMember().getMemberId());
    Board board = boardService.createBoard(mapper.createBoardDtoToBoard(createBoardDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.boardToBoardInfoDto(board),
      "SUCCESS"),
      HttpStatus.CREATED);
  }

  /**
   * 게시글 변경
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @PatchMapping("/{board-id}")
  public ResponseEntity<SingleResponseWithMessageDto> updateBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  @Positive @PathVariable("board-id") long boardId,
                                                                  @Valid @RequestBody UpdateBoardDto updateBoardDto) {
    updateBoardDto.setBoardId(boardId);
    updateBoardDto.setMemberId(customUserDetails.getMember().getMemberId());
    Board board = boardService.updateBoard(mapper.updateBoardToBoard(updateBoardDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.boardToBoardInfoDto(board),
      "SUCCESS"),
      HttpStatus.OK);
  }

  /**
   * 게시글 삭제
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @DeleteMapping("/{board-id}")
  public ResponseEntity deleteBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @Positive @PathVariable("board-id") long boardId) {
    boardService.deleteBoardByIdAndMemberId(boardId, customUserDetails.getMember().getMemberId());

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * 내가 쓴 게시글 조회
   */
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ResponseEntity<MultiResponseWithPageInfoDto> getMyBoards(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  @Positive @PathParam("page") int page,
                                                                  @Positive @PathParam("size") int size) {
    Page<Board> boardRestPage
      = boardService.getMyBoards(customUserDetails.getMember().getMemberId(), page - 1, size);
    List<Board> boardList = boardRestPage.getContent();

    return new ResponseEntity(new MultiResponseWithPageInfoDto<>(mapper.boardListToBoardDtoList(boardList),
      boardRestPage),
      HttpStatus.OK);
  }
}
