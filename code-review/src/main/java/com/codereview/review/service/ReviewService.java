package com.codereview.review.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.common.helper.RestPage;
import com.codereview.review.entity.Approve;
import com.codereview.review.entity.Review;
import com.codereview.review.repository.ReviewRepository;
import com.codereview.reviewer.service.ReviewerService;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final CustomBeanUtils<Review> beanUtils;
  private final ReviewerService reviewerService;

  public Review enrollReview(Review review) {
    reviewerService.existsReviewerById(review.getReviewer().getReviewerId());
    return reviewRepository.save(review);
  }

  public Review updateReview(Review review) {
    Review findReview = findVerifiedReview(review.getReviewId());
    if(!Objects.equals(findReview.getMember().getMemberId(), review.getMember().getMemberId())) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    Review saveReview = beanUtils.copyNonNullProperties(review, findReview);
    return reviewRepository.save(saveReview);
  }

  @Transactional(readOnly = true)
  public void deleteReview(Long reviewId, Long memberId) {
    Review findReview = findVerifiedReview(reviewId);
    if(!Objects.equals(findReview.getMember().getMemberId(), memberId)) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    reviewRepository.delete(findReview);
  }

  @Transactional(readOnly = true)
  public RestPage<Review> getRequiringReviews(Long memberId, Pageable pageable) {
    return reviewRepository.findAllByMemberId(memberId, pageable);
  }

  @Transactional(readOnly = true)
  private Review findVerifiedReview(Long reviewId) {
    Optional<Review> optionalReview = reviewRepository.findById(reviewId);
    return optionalReview.orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RestPage<Review> getRequiredReviews(Long reviewerId, Pageable pageable) {
    return reviewRepository.findAllByReviewerId(reviewerId, pageable);
  }

  public void approveReview(long reviewId, Long memberId) {
    Review findReview = findVerifiedReview(reviewId);
    if(!Objects.equals(findReview.getMember().getMemberId(), memberId)) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    findReview.setApprove(Approve.Y);
    reviewRepository.save(findReview);
  }

  public void refuseReview(long reviewId, Long memberId) {
    Review findReview = findVerifiedReview(reviewId);
    if(!Objects.equals(findReview.getMember().getMemberId(), memberId)) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    findReview.setApprove(Approve.N);
    reviewRepository.save(findReview);
  }
}
