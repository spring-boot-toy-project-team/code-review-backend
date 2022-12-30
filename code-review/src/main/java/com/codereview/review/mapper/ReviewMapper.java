package com.codereview.review.mapper;

import com.codereview.review.dto.ReviewRequestDto;
import com.codereview.review.dto.ReviewResponseDto;
import com.codereview.review.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
  Review createReviewDtoToReview(ReviewRequestDto.CreateReviewDto createReviewDto);

  Review updateReviewDtoToReview(ReviewRequestDto.UpdateReviewDto updateReviewDto);

  ReviewResponseDto.ReviewInfoDto reviewToReviewInfoDto(Review review);

  List<ReviewResponseDto.ReviewInfoDto> reviewListToReviewInfoDtoList(List<Review> reviewList);
}
