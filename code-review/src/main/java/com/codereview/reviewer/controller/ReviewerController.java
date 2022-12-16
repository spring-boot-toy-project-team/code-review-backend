package com.codereview.reviewer.controller;

import com.codereview.common.dto.response.MultiResponseWithPageInfoDto;
import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.common.helper.RestPage;
import com.codereview.reviewer.dto.ReviewerRequestDto.CreateReviewerDto;
import com.codereview.reviewer.entity.Reviewer;
import com.codereview.reviewer.mapper.ReviewerMapper;
import com.codereview.reviewer.service.ReviewerService;
import com.codereview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviewer")
@RequiredArgsConstructor
public class ReviewerController {
  private final ReviewerService reviewerService;
  private final ReviewerMapper mapper;

  @GetMapping
  public ResponseEntity<MultiResponseWithPageInfoDto> getReviewerList(
    @PageableDefault(size = 10, sort = "reviewerId", direction = Sort.Direction.DESC) Pageable pageable) {
    RestPage<Reviewer> reviewerRestPage = reviewerService.getReviewerList(pageable);
    List<Reviewer> reviewerList = reviewerRestPage.getContent();

    return new ResponseEntity<>(new MultiResponseWithPageInfoDto(mapper.reviewerListToReviewerShortInfoList(reviewerList),
      reviewerRestPage),
      HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SingleResponseWithMessageDto> enrollReviewer(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                     @Valid @RequestBody CreateReviewerDto reviewerCreateDto) {
    reviewerCreateDto.setMemberId(customUserDetails.getMember().getMemberId());
    Reviewer reviewer = reviewerService.enroll(mapper.createReviewerDtoToReviewer(reviewerCreateDto));

    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.reviewerToReviewerInfoDto(reviewer),
      "SUCCESS"),
      HttpStatus.CREATED);
  }

  @DeleteMapping("/{reviewer-id}")
  public ResponseEntity deleteReviewer(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @Min(value = 1L) @PathVariable("reviewer-id") Long reviewerId) {
    reviewerService.deleteReviewer(reviewerId, customUserDetails.getMember().getMemberId());

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/career")
  public ResponseEntity<SingleResponseWithMessageDto> getReviewerCareer() {

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(reviewerService.getCareers(),
      "SUCCESS"),
      HttpStatus.OK);
  }

  @GetMapping("/position")
  public ResponseEntity<SingleResponseWithMessageDto> getReviewerPosition() {

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(reviewerService.getPositions(),
      "SUCCESS"),
      HttpStatus.OK);
  }

  // TODO: 리뷰어 검색 기능 추가
}
