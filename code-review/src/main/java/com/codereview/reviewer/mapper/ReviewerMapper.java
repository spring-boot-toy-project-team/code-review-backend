package com.codereview.reviewer.mapper;

import com.codereview.member.entity.Member;
import com.codereview.reviewer.dto.ReviewerRequestDto;
import com.codereview.reviewer.dto.ReviewerResponseDto;
import com.codereview.reviewer.entity.Career;
import com.codereview.reviewer.entity.Position;
import com.codereview.reviewer.entity.Reviewer;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {
  default Reviewer createReviewerDtoToReviewer(ReviewerRequestDto.CreateReviewerDto reviewerCreateDto) {
    return Reviewer.builder()
      .career(Career.valueOf(reviewerCreateDto.getCareer().toUpperCase()))
      .member(Member.builder()
        .memberId(reviewerCreateDto.getMemberId())
        .build()
      )
      .introduction(reviewerCreateDto.getIntroduction())
      .skills(reviewerCreateDto.getSkills().stream()
        .map(String::toLowerCase)
        .collect(Collectors.toSet())
      )
      .position(reviewerCreateDto.getPosition().stream()
        .map(position-> Position.valueOf(position.toUpperCase()).getDescription())
        .collect(Collectors.joining(", "))
      )
      .build();
  }

  ReviewerResponseDto.ReviewerInfoDto reviewerToReviewerInfoDto(Reviewer reviewer);

  ReviewerResponseDto.ReviewerShortInfoDto reviewerToReviewerShortInfoDto(Reviewer reviewer);

  default List<ReviewerResponseDto.ReviewerShortInfoDto> reviewerListToReviewerShortInfoList(List<Reviewer> reviewerList) {
    return reviewerList.stream()
      .map(this::reviewerToReviewerShortInfoDto)
      .collect(Collectors.toList());
  }

  default Reviewer updateReviewerDtoToReviewer(ReviewerRequestDto.UpdateReviewerDto updateReviewerDto) {
    return Reviewer.builder()
      .reviewerId(updateReviewerDto.getReviewerId())
      .career(Career.valueOf(updateReviewerDto.getCareer().toUpperCase()))
      .member(Member.builder()
        .memberId(updateReviewerDto.getMemberId())
        .build()
      )
      .introduction(updateReviewerDto.getIntroduction())
      .skills(updateReviewerDto.getSkills().stream()
        .map(String::toLowerCase)
        .collect(Collectors.toSet())
      )
      .position(updateReviewerDto.getPosition().stream()
        .map(position-> Position.valueOf(position.toUpperCase()).getDescription())
        .collect(Collectors.joining(", "))
      )
      .build();
  }
}
