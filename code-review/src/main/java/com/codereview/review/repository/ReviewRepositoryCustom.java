package com.codereview.review.repository;

import com.codereview.common.helper.RestPage;
import com.codereview.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepositoryCustom {
  RestPage<Review> findAllByMemberId(Long memberId, Pageable pageable);

  RestPage<Review> findAllByReviewerId(Long reviewerId, Pageable pageable);
}
