package com.codereview.board.service;


import com.codereview.board.entity.Board;
import com.codereview.board.repository.board.BoardRepository;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.helper.RestPage;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final BoardTagService boardTagService;
  private final CustomBeanUtils<Board> beanUtils;

  /**
   * 게시판 조회
   */
  @Transactional(readOnly = true)
  @Cacheable(key = "{#page, #size}", value = "getBoards")
  public RestPage<Board> getBoards(Pageable pageable) {
    return new RestPage<>(boardRepository.findAll(
      pageable
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
  @CacheEvict(cacheNames = {"getBoards"}, allEntries = true)
  public Board createBoard(Board board) {
    boardTagService.createMultipleBoardTag(board);
    return boardRepository.save(board);
  }

  /**
   * 게시글 변경
   */
  @CacheEvict(cacheNames = {"getBoards"}, allEntries = true)
  public Board updateBoard(Board board) {
    Board findBoard = findVerifiedBoardWithMemberId(board.getBoardId(), board.getMember().getMemberId());
    boardTagService.updateMultipleBoardTag(board);
    boardTagService.deleteOldBoardTag(board, findBoard);
    Board saveBoard = beanUtils.copyNonNullProperties(board, findBoard);

    return boardRepository.save(saveBoard);
  }

  @CacheEvict(cacheNames = {"getBoards"}, allEntries = true)
  public void deleteBoardByIdAndMemberId(long boardId, long memberId) {
    Board findBoard = findVerifiedBoardWithMemberId(boardId, memberId);
    boardRepository.delete(findBoard);
  }

  public Page<Board> getMyBoards(Long memberId, Pageable pageable) {
    return boardRepository.findAllByMemberId(memberId, pageable);
  }
}
