package com.codereview.comment.repository;

import com.codereview.comment.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codereview.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Comment> findAllByBoardId(long boardId, Pageable pageable) {
    List<Comment> content = jpaQueryFactory
            .select(comment)
            .from(comment)
            .where(comment.board.boardId.eq(boardId))
            .orderBy(comment.createdAt.desc())
            .fetch();
    return new PageImpl<>(content, pageable, content.size());
  }
}
