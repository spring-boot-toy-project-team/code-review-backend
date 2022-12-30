package com.codereview.review.controller;

import com.codereview.common.dto.response.MessageResponseDto;
import com.codereview.common.dto.response.MultiResponseWithPageInfoDto;
import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.common.helper.RestPage;
import com.codereview.review.dto.ReviewRequestDto.*;
import com.codereview.review.entity.Review;
import com.codereview.review.mapper.ReviewMapper;
import com.codereview.review.service.ReviewService;
import com.codereview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewService reviewService;
  private final ReviewMapper mapper;

  @PostMapping
  public ResponseEntity<SingleResponseWithMessageDto> enrollReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   @Valid @RequestBody CreateReviewDto createReviewDto) {
    createReviewDto.setMemberId(customUserDetails.getMember().getMemberId());
    Review review = reviewService.enrollReview(mapper.createReviewDtoToReview(createReviewDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(mapper.reviewToReviewInfoDto(review),
      "SUCCESS"),
      HttpStatus.CREATED);
  }

  @PatchMapping("/{review-id}")
  ResponseEntity<SingleResponseWithMessageDto> updateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @Positive @PathVariable("review-id") long reviewId,
                                                            @Valid @RequestBody UpdateReviewDto updateReviewDto) {
    updateReviewDto.setReviewId(reviewId);
    updateReviewDto.setMemberId(customUserDetails.getMember().getMemberId());
    Review review = reviewService.updateReview(mapper.updateReviewDtoToReview(updateReviewDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(mapper.reviewToReviewInfoDto(review),
      "SUCCESS"),
      HttpStatus.OK);
  }

  @DeleteMapping("/{review-id}")
  public ResponseEntity deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     @Positive @PathVariable("review-id") long reviewId) {
    reviewService.deleteReview(reviewId, customUserDetails.getMember().getMemberId());

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping
  public ResponseEntity<MultiResponseWithPageInfoDto> getRequiringReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                          @PageableDefault(size = 10,
                                                                           sort = "reviewId",
                                                                           direction = Sort.Direction.DESC) Pageable pageable) {
    RestPage<Review> reviewRestPage = reviewService.getRequiringReviews(customUserDetails.getMember().getMemberId(), pageable);
    List<Review> reviewList = reviewRestPage.getContent();

    return new ResponseEntity<>(new MultiResponseWithPageInfoDto<>(mapper.reviewListToReviewInfoDtoList(reviewList),
      reviewRestPage),
      HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<MultiResponseWithPageInfoDto> getRequiredReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                         @PageableDefault(size = 10,
                                                                          sort = "reviewId",
                                                                          direction = Sort.Direction.DESC) Pageable pageable){
    RestPage<Review> reviewRestPage = reviewService.getRequiredReviews(customUserDetails.getMember().getMemberId(), pageable);
    List<Review> reviewList = reviewRestPage.getContent();

    return new ResponseEntity<>(new MultiResponseWithPageInfoDto<>(mapper.reviewListToReviewInfoDtoList(reviewList),
      reviewRestPage),
      HttpStatus.OK);
  }

  @PatchMapping("/{review-id}/me/approve")
  ResponseEntity<MessageResponseDto> approveReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @Positive @PathVariable("review-id") long reviewId) {
    reviewService.approveReview(reviewId, customUserDetails.getMember().getMemberId());

    return new ResponseEntity<>(new MessageResponseDto("SUCCESS"),
      HttpStatus.OK);
  }

  @PatchMapping("/{review-id}/me/refuse")
  ResponseEntity<MessageResponseDto> refuseReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @Positive @PathVariable("review-id") long reviewId) {

    reviewService.refuseReview(reviewId, customUserDetails.getMember().getMemberId());

    return new ResponseEntity<>(new MessageResponseDto("SUCCESS"),
      HttpStatus.OK);
  }
}
