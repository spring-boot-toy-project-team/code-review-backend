package com.codereview.reviewer.repository;

import com.codereview.reviewer.entity.Reviewer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.codereview.reviewer.entity.QReviewer.reviewer;

@Repository
@RequiredArgsConstructor
public class ReviewerRepositoryImpl implements ReviewerRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<Reviewer> findByMemberId(Long memberId) {
    return Optional.ofNullable(
        jpaQueryFactory
          .select(reviewer)
          .from(reviewer)
          .where(reviewer.member.memberId.eq(memberId))
          .fetchOne()
      );
  }
}
