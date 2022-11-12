package com.codereview.board.repository.boardTags;

import com.codereview.board.entity.BoardTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codereview.board.entity.QBoardTag.boardTag;


@Repository
@RequiredArgsConstructor
public class BoardTagRepositoryImpl implements BoardTagRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean existsByBoardIdAndTagId(long boardId, long tagId) {
    return jpaQueryFactory.select(boardTag)
      .from(boardTag)
      .where(boardTag.board.boardId.eq(boardId)
        .and(boardTag.tag.tagId.eq(tagId)))
      .fetchFirst() != null;
  }

  @Override
  public List<BoardTag> findAllByBoardId(long boardId) {
    return jpaQueryFactory.select(boardTag)
      .from(boardTag)
      .where(boardTag.board.boardId.eq(boardId))
      .fetch();
  }
}
