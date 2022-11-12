package com.codereview.board.service;

import com.codereview.board.entity.Board;
import com.codereview.board.entity.BoardTag;
import com.codereview.board.repository.boardTags.BoardTagRepository;
import com.codereview.tag.entity.Tag;
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

  public BoardTag createBoardTag(BoardTag boardTag) {
    boardTag.setTag(tagService.findTagOrSave(boardTag.getTag()));
    return boardTagRepository.save(boardTag);
  }

  public BoardTag updateBoardTag(BoardTag boardTag) {
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
   * @param newBoard 새로운 BoardTag
   * @param oldBoard 기존의 BoardTag
   */
  public void deleteOldBoardTag(Board newBoard, Board oldBoard) {
    List<String> newTag = newBoard.getBoardTags().stream()
      .map(boardTag -> boardTag.getTag().getName())
      .collect(Collectors.toList());
    oldBoard.getBoardTags().stream()
      .filter(boardTag -> !newTag.contains(boardTag.getTag().getName()))
      .forEach(this::deleteBoardTag);
    oldBoard.setBoardTags(newBoard.getBoardTags());
  }
}
