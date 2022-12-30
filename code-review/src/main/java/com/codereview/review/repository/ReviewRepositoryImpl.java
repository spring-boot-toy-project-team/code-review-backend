package com.codereview.review.repository;

import com.codereview.common.helper.RestPage;
import com.codereview.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codereview.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public RestPage<Review> findAllByMemberId(Long memberId, Pageable pageable) {
    List<Review> content = jpaQueryFactory
      .select(review)
      .from(review)
      .where(review.member.memberId.eq(memberId))
      .fetch();
    return new RestPage<>(new PageImpl<>(content, pageable, content.size()));
  }

  @Override
  public RestPage<Review> findAllByReviewerId(Long reviewerId, Pageable pageable) {
    List<Review> content = jpaQueryFactory
      .select(review)
      .from(review)
      .where(review.reviewer.reviewerId.eq(reviewerId))
      .fetch();
    return new RestPage<>(new PageImpl<>(content, pageable, content.size()));
  }
}
