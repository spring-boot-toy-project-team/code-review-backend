package com.codereview.board.repository.boardTags;

import com.codereview.board.entity.BoardTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardTagRepositoryCustom {
  boolean existsByBoardIdAndTagId(long boardId, long tagId);

  List<BoardTag> findAllByBoardId(long boardId);
}
