package com.codereview.reviewer.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.common.helper.RestPage;
import com.codereview.reviewer.entity.Career;
import com.codereview.reviewer.entity.Position;
import com.codereview.reviewer.entity.Reviewer;
import com.codereview.reviewer.repository.ReviewerRepository;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewerService {
  private final ReviewerRepository reviewerRepository;
  private final CustomBeanUtils<Reviewer> beanUtils;

  public Reviewer enroll(Reviewer reviewer) {
    existsReviewerByMemberId(reviewer.getMember().getMemberId());
    return reviewerRepository.save(reviewer);
  }

  @Transactional(readOnly = true)
  private void existsReviewerByMemberId(Long memberId) {
    Optional<Reviewer> optionalReviewer = reviewerRepository.findByMemberId(memberId);
    if(optionalReviewer.isPresent())
      throw new BusinessLogicException(ExceptionCode.REVIEWER_ALREADY_EXISTS);
  }

  public void deleteReviewer(Long reviewerId, Long memberId) {
    Reviewer findReviewer = findVerifiedReviewerByMemberId(reviewerId);
    if(!Objects.equals(findReviewer.getMember().getMemberId(), memberId)) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    reviewerRepository.delete(findReviewer);
  }
  @Transactional(readOnly = true)
  private Reviewer findVerifiedReviewerByMemberId(Long memberId) {
    Optional<Reviewer> optionalReviewer = reviewerRepository.findByMemberId(memberId);
    return optionalReviewer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEWER_NOT_FOUND));
  }

  public  Map<String, String> getCareers() {
    return Arrays.stream(Career.values())
      .collect(Collectors.toMap(Career::name, Career::getDescription));
  }

  public Map<String, String> getPositions() {
    return Arrays.stream(Position.values())
      .collect(Collectors.toMap(Position::name, Position::getDescription));
  }

  @Transactional(readOnly = true)
  public RestPage<Reviewer> getReviewers(Pageable pageable) {
    return new RestPage<>(reviewerRepository.findAll(pageable));
  }

  public Reviewer updateReviewer(Reviewer reviewer) {
    Reviewer findReviewer = findVerifiedReviewer(reviewer.getReviewerId());
    if(!Objects.equals(findReviewer.getMember().getMemberId(), reviewer.getMember().getMemberId())) {
      throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
    }
    Reviewer saveReviewer = beanUtils.copyNonNullProperties(reviewer, findReviewer);
    return reviewerRepository.save(saveReviewer);
  }

  @Transactional(readOnly = true)
  public Reviewer getReviewer(Long reviewerId) {
    return findVerifiedReviewer(reviewerId);
  }

  @Transactional(readOnly = true)
  private Reviewer findVerifiedReviewer(Long reviewerId) {
    Optional<Reviewer> optionalReviewer = reviewerRepository.findById(reviewerId);
    return optionalReviewer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEWER_NOT_FOUND));
  }
}
