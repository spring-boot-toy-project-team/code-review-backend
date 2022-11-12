package com.codereview.board.repository.board;

import com.codereview.board.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codereview.board.entity.QBoard.board;
import static com.codereview.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Board> findAllByMemberId(long memberId, Pageable pageable) {
    List<Board> content = jpaQueryFactory
      .select(board)
      .from(board)
      .leftJoin(board.member, member)
      .on(member.memberId.eq(memberId))
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch();
    return new PageImpl<>(content, pageable, content.size());
  }
}
