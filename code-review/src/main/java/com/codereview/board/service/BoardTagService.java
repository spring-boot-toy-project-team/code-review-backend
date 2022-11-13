package com.codereview.board.service;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.boardTags.BoardTagRepository;
import com.codereview.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardTagService {
  private final TagService tagService;
  private final BoardTagRepository boardTagRepository;

  public void createMultipleBoardTag(Board board) {
    board.setBoardTags(board.getBoardTags().stream()
      .peek(boardTag -> {
        boardTag.setBoard(board);
        boardTag.setTag(tagService.findTagOrSave(boardTag.getTag()));
      })
      .collect(Collectors.toList()));
  }

  private BoardTag updateBoardTag(BoardTag boardTag) {
    boardTag.setTag(tagService.findTagOrSave(boardTag.getTag()));
    if(existBoardTagByBoardIdAndTagId(boardTag.getBoard().getBoardId(), boardTag.getTag().getTagId()))
      return boardTag;
    return boardTagRepository.saveAndFlush(boardTag);
  }

  @Transactional(readOnly = true)
  private boolean existBoardTagByBoardIdAndTagId(long boardId, long tagId) {
    return boardTagRepository.existsByBoardIdAndTagId(boardId, tagId);
  }

  @Transactional(readOnly = true)
  public List<BoardTag> findBoardTagsByBoardId(long boardId) {
    return boardTagRepository.findAllByBoardId(boardId);
  }

  public void deleteBoardTag(BoardTag boardTag) {
    boardTagRepository.delete(boardTag);
  }

  /**
   * 기존의 BoardTag 업데이트
   * @param newBoard: 새로운 BoardTag
   * @param oldBoard: 기존의 BoardTag
   */
  public void deleteOldBoardTag(Board newBoard, Board oldBoard) {
    List<String> newTag = getNameOfTagsFromBoard(newBoard);
    deleteBoardTagIfNotContainsTagName(oldBoard, newTag);
    oldBoard.setBoardTags(newBoard.getBoardTags());
  }

  /**
   * 게시글로부터 태그 이름 추출하는 함수
   * @param board: 추출할 게시글
   * @return
   */
  private List<String> getNameOfTagsFromBoard(Board board) {
    return board.getBoardTags().stream()
      .map(boardTag -> boardTag.getTag().getName())
      .collect(Collectors.toList());
  }

  /**
   * 게시글로부터 태그 이름 리스트에 포함되지 않으면 삭제하는 함수
   * @param board: 비교할 게시글
   * @param tagList: 태그 이름 리스트
   */
  private void deleteBoardTagIfNotContainsTagName(Board board, List<String> tagList) {
    board.getBoardTags().stream()
      .filter(boardTag -> !tagList.contains(boardTag.getTag().getName()))
      .forEach(this::deleteBoardTag);
  }

  public void updateMultipleBoardTag(Board board) {
    board.setBoardTags(board.getBoardTags().stream()
      .map(boardTag -> {
        boardTag.setBoard(board);
        return this.updateBoardTag(boardTag);
      })
      .collect(Collectors.toList())
    );
  }
}
