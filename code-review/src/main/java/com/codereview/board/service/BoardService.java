package com.codereview.board.service;


import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.helper.RestPage;
import com.codereview.tag.service.TagService;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final BoardTagService boardTagService;
  private final CustomBeanUtils<Board> beanUtils;
  private final TagService tagService;

  /**
   * 게시판 조회
   */
  @Transactional(readOnly = true)
  public RestPage<Board> getBoards(int page, int size) {
    return new RestPage<>(boardRepository.findAll(
      PageRequest.of(
        page,
        size,
        Sort.by("boardId").descending()
      )
    ));
  }

  /**
   * 게시글 상세 조회
   */
  @Transactional(readOnly = true)
  public Board getBoard(long boardId) {
    return findVerifiedBoard(boardId);
  }

  /**
   * 유효한 게시글 상세 조회
   */
  @Transactional(readOnly = true)
  private Board findVerifiedBoard(long boardId) {
    Optional<Board> optionalBoard = boardRepository.findById(boardId);
    return optionalBoard.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
  }

  /**
   * 권한 있는 게시글 상세 조회
   */
  @Transactional(readOnly = true)
  private Board findVerifiedBoardWithMemberId(long boardId, long memberId) {
    Board findBoard = findVerifiedBoard(boardId);
    if(findBoard.getMember().getMemberId() != memberId)
      throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
    return findBoard;
  }

  /**
   * 게시글 저장
   */
  public Board createBoard(Board board) {
    // TODO: 리펙토링 가능해서 좀 더 다듬기 필요!, 추상화 수준 맞추기
    board.setBoardTags(board.getBoardTags().stream()
      .peek(boardTag -> {
        boardTag.setBoard(board);
        boardTag.setTag(tagService.findTagOrSave(boardTag.getTag()));
      })
      .collect(Collectors.toList()));
    return boardRepository.save(board);
  }

  /**
   * 게시글 변경
   */
  public Board updateBoard(Board board) {
    // TODO: TAG와 연관관계 매핑 할 것
    Board findBoard = findVerifiedBoardWithMemberId(board.getBoardId(), board.getMember().getMemberId());

    // 저장 및 유지
    board.setBoardTags(board.getBoardTags().stream()
        .map(boardTag -> {
          boardTag.setBoard(board);
          return boardTagService.updateBoardTag(boardTag);
        })
      .collect(Collectors.toList())
    );
    boardTagService.deleteOldBoardTag(board, findBoard);
    Board saveBoard = beanUtils.copyNonNullProperties(board, findBoard);

    return boardRepository.save(saveBoard);
  }

  public void deleteBoardByIdAndMemberId(long boardId, long memberId) {
    Board findBoard = findVerifiedBoardWithMemberId(boardId, memberId);
    boardRepository.delete(findBoard);
  }

  public Page<Board> getMyBoards(Long memberId, int page, int size) {
    return boardRepository.findAllByMemberId(memberId, PageRequest.of(page, size, Sort.by("boardId").descending()));
  }
}
