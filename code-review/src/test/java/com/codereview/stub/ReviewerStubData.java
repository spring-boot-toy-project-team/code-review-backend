package com.codereview.stub;

import com.codereview.common.helper.RestPage;
import com.codereview.member.entity.Member;
import com.codereview.reviewer.dto.ReviewerRequestDto.*;
import com.codereview.reviewer.dto.ReviewerResponseDto.*;
import com.codereview.reviewer.entity.Career;
import com.codereview.reviewer.entity.Position;
import com.codereview.reviewer.entity.Reviewer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReviewerStubData {
  public static Reviewer reviewer(Member member) {
    return Reviewer.builder()
      .reviewerId(1L)
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .member(member)
      .career(Career.JUNIOR)
      .position(Position.BACK_END.name())
      .build();
  }

  public static RestPage<Reviewer> reviewerRestPage(int page, int size) {
    return new RestPage<>(new PageImpl<>(List.of(reviewer(MemberStubData.member(1L)),
      reviewer(MemberStubData.member(2L)),
      reviewer(MemberStubData.member(3L))
    ), PageRequest.of(page, size, Sort.by("reviewerId").descending()), 3));
  }

  public static ReviewerShortInfoDto reviewerToReviewerShortInfoDto(Reviewer reviewer) {
    return ReviewerShortInfoDto.builder()
      .career(reviewer.getCareer().name())
      .position(reviewer.getPosition())
      .reviewerId(reviewer.getReviewerId())
      .skills(reviewer.getSkills())
      .build();
  }

  public static List<ReviewerShortInfoDto> reviewerListToReviewerShortInfoDto(List<Reviewer> reviewerList) {
    return reviewerList.stream()
      .map(ReviewerStubData::reviewerToReviewerShortInfoDto)
      .collect(Collectors.toList());
  }

  public static ReviewerInfoDto reviewerToReviewerInfoDto(Reviewer reviewer) {
    return ReviewerInfoDto.builder()
      .career(reviewer.getCareer().name())
      .introduction(reviewer.getIntroduction())
      .position(reviewer.getPosition())
      .reviewerId(reviewer.getReviewerId())
      .skills(reviewer.getSkills())
      .build();
  }

  public static CreateReviewerDto createReviewerDto(Member member) {
    return CreateReviewerDto.builder()
      .career(Career.JUNIOR.name())
      .position(Set.of(Position.BACK_END.name()))
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .memberId(member.getMemberId())
      .build();
  }

  public static Reviewer updatedReviewer(Member member) {
    return Reviewer.builder()
      .reviewerId(1L)
      .introduction("hello")
      .skills(Set.of("Django", "Vue.js"))
      .member(member)
      .career(Career.MID_LEVEL)
      .position(Position.BACK_END.name() + ", " + Position.DEVOPS.name())
      .build();
  }

  public static UpdateReviewerDto updateReviewerDto() {
    return UpdateReviewerDto.builder()
      .reviewerId(1L)
      .introduction("hello")
      .skills(Set.of("Django", "Vue.js"))
      .memberId(1L)
      .career(Career.MID_LEVEL.name())
      .position(Set.of(Position.BACK_END.name(), Position.DEVOPS.name()))
      .build();
  }

  public static Map<String, String> careers() {
    return Arrays.stream(Career.values())
      .collect(Collectors.toMap(Career::name, Career::getDescription));
  }

  public static Map<String, String> positions() {
    return Arrays.stream(Position.values())
      .collect(Collectors.toMap(Position::name, Position::getDescription));
  }
}
