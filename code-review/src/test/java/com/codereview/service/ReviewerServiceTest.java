package com.codereview.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.helper.RestPage;
import com.codereview.member.entity.Member;
import com.codereview.reviewer.entity.Career;
import com.codereview.reviewer.entity.Position;
import com.codereview.reviewer.entity.Reviewer;
import com.codereview.reviewer.repository.ReviewerRepository;
import com.codereview.reviewer.service.ReviewerService;
import com.codereview.stub.MemberStubData;
import com.codereview.stub.ReviewerStubData;
import com.codereview.util.CustomBeanUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewerServiceTest {
  @Spy
  @InjectMocks
  private ReviewerService reviewerService;

  @Mock
  private ReviewerRepository reviewerRepository;

  @Mock
  private CustomBeanUtils<Reviewer> beanUtils;

  @Nested
  @DisplayName("리뷰어 등록 테스트")
  public class enrollTest {
    private Member member;

    @BeforeEach
    public void beforeEach() throws Exception {
      member = MemberStubData.member();
    }
    @Test
    @DisplayName("리뷰어 등록 성공 테스트")
    public void enrollSuccessTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.empty());
      given(reviewerRepository.save(Mockito.any(Reviewer.class))).willReturn(reviewer);

      // when
       Reviewer savedReviewer = reviewerService.enroll(reviewer);

      // then
      Assertions.assertThat(reviewer).isEqualTo(savedReviewer);
    }

    @Test
    @DisplayName("리뷰어 등록 실패 테스트")
    public void enrollFailTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.enroll(reviewer))
        .isInstanceOf(BusinessLogicException.class);
    }
  }

  @Nested
  @DisplayName("회원 식별자로 등록된 리뷰어 조회 테스트")
  public class existsReviewerByMemberIdTest {
    private Member member;

    @BeforeEach
    public void beforeEach() throws Exception {
      member = MemberStubData.member();
    }

    @Test
    @DisplayName("회원 식별자로 등록된 리뷰어 존재에 따른 에러 발생")
    public void existsReviewerByMemberIdExceptionTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      Method existsReviewerByMemberIdMethod = ReviewerService.class.getDeclaredMethod("existsReviewerByMemberId", Long.class);
      existsReviewerByMemberIdMethod.setAccessible(true);

      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when

      // then
      Assertions.assertThatThrownBy(() -> existsReviewerByMemberIdMethod.invoke(reviewerService, reviewer.getReviewerId()))
        .isInstanceOf(InvocationTargetException.class);
    }

    @Test
    @DisplayName("회원 식별자로 등록된 리뷰어 미존재 테스트")
    public void existsReviewerByMemberIdNothingTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      Method existsReviewerByMemberIdMethod = ReviewerService.class.getDeclaredMethod("existsReviewerByMemberId", Long.class);
      existsReviewerByMemberIdMethod.setAccessible(true);

      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      existsReviewerByMemberIdMethod.invoke(reviewerService, reviewer.getReviewerId());
    }
  }

  @Nested
  @DisplayName("리뷰어 식별자로 등록된 리뷰어 조회 테스트")
  public class existsReviewerByIdTest {
    private Member member;

    @BeforeEach
    public void beforeEach() throws Exception {
      member = MemberStubData.member();
    }

    @Test
    @DisplayName("리뷰 식별자로 등록된 리뷰어 존재 테스트")
    public void existsReviewerByIdExceptionTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when

      // then
      reviewerService.existsReviewerById(reviewer.getReviewerId());
    }

    @Test
    @DisplayName("리뷰 식별자로 등록된 리뷰어 미존재 테스트")
    public void existsReviewerByIdNotFoundTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.existsReviewerById(reviewer.getReviewerId()))
        .isInstanceOf(BusinessLogicException.class);
    }
  }
  
  @Nested
  @DisplayName("리뷰어 삭제 테스트")
  public class deleteReviewerTest {
    private Member member = MemberStubData.member();

    @Test
    @DisplayName("리뷰어 삭제 성공 테스트")
    public void deleteReviewerSuccessTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when
      reviewerService.deleteReviewer(reviewer.getReviewerId(), member.getMemberId());

      // then
      verify(reviewerRepository).delete(argThat(rev -> Objects.equals(rev.getReviewerId(), reviewer.getReviewerId())));
    }

    @Test
    @DisplayName("리뷰어 삭제 실패 테스트 - 리뷰어 조회 실패")
    public void deleteReviewerFailByNotFoundTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.deleteReviewer(reviewer.getReviewerId(), member.getMemberId()))
        .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("리뷰어 삭제 실패 테스트 - 삭제 권한 없음")
    public void deleteReviewerFailByForbiddenTest() throws Exception {
      // given
      Member otherMember = MemberStubData.member(2L);
      Reviewer reviewer = ReviewerStubData.reviewer(otherMember);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.deleteReviewer(reviewer.getReviewerId(), member.getMemberId()))
        .isInstanceOf(BusinessLogicException.class);
    }
  }

  @Nested
  @DisplayName("회원 식별자로 리뷰어 조회 테스트")
  public class findVerifiedReviewerByMemberIdTest {
    private Member member = MemberStubData.member();

    @Test
    @DisplayName("회원 식별자로 리뷰어 조회 성공 테스트")
    public void findVerifiedReviewerByMemberIdSuccessTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      Method findVerifiedReviewerByMemberIdMethod
        = ReviewerService.class.getDeclaredMethod("findVerifiedReviewerByMemberId", Long.class);
      findVerifiedReviewerByMemberIdMethod.setAccessible(true);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when
      Reviewer findReviewer = (Reviewer) findVerifiedReviewerByMemberIdMethod.invoke(reviewerService, member.getMemberId());

      // then
      Assertions.assertThat(reviewer).isEqualTo(findReviewer);
    }

    @Test
    @DisplayName("회원 식별자로 리뷰어 조회 실패 테스트")
    public void findVerifiedReviewerByMemberIdFailTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      Method findVerifiedReviewerByMemberIdMethod
        = ReviewerService.class.getDeclaredMethod("findVerifiedReviewerByMemberId", Long.class);
      findVerifiedReviewerByMemberIdMethod.setAccessible(true);
      given(reviewerRepository.findByMemberId(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      Assertions.assertThatThrownBy(() -> findVerifiedReviewerByMemberIdMethod.invoke(reviewerService, member.getMemberId()))
        .isInstanceOf(InvocationTargetException.class);
    }
  }

  @Test
  @DisplayName("경력 사항 조회 테스트")
  public void getCareersTest() throws Exception {
    // given
    Map<String, String> careerMap = Arrays.stream(Career.values())
      .collect(Collectors.toMap(Career::name, Career::getDescription));

    // when
    Map<String, String> getCareers = reviewerService.getCareers();

    // then
    Assertions.assertThat(careerMap).isEqualTo(getCareers);
  }

  @Test
  @DisplayName("도메인 조회 테스트")
  public void getPositionsTest() throws Exception {
    // given
    Map<String, String> positionMap = Arrays.stream(Position.values())
      .collect(Collectors.toMap(Position::name, Position::getDescription));

    // when
    Map<String, String> getPositions = reviewerService.getPositions();

    // then
    Assertions.assertThat(positionMap).isEqualTo(getPositions);
  }

  @Test
  @DisplayName("리뷰어들 조회 테스트")
  public void getReviewersTest() throws Exception {
    // given
    int page = 1, size = 10;
    RestPage<Reviewer> reviewerRestPage = ReviewerStubData.reviewerRestPage(page, size);
    Pageable pageable = PageRequest.of(page, size, Sort.by("reviewerId").descending());
    given(reviewerRepository.findAll(Mockito.any(Pageable.class))).willReturn(reviewerRestPage);

    // when
    RestPage<Reviewer> reviewers = reviewerService.getReviewers(pageable);

    // then
    Assertions.assertThat(reviewerRestPage.getContent().size()).isEqualTo(reviewers.getContent().size());
  }

  @Nested
  @DisplayName("리뷰어 조회 테스트")
  public class getReviewer {
    private Member member = MemberStubData.member();
    @Test
    @DisplayName("리뷰어 조회 성공 테스트")
    public void getReviewerSuccessTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when
      Reviewer findReviewer = reviewerService.getReviewer(reviewer.getReviewerId());

      // then
      Assertions.assertThat(reviewer).isEqualTo(findReviewer);
    }

    @Test
    @DisplayName("리뷰어 조회 실패 테스트")
    public void getReviewerFailTest() throws Exception {
      // given
      Long reviewerId = 0L;
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.getReviewer(reviewerId))
        .isInstanceOf(BusinessLogicException.class);
    }
  }

  @Nested
  @DisplayName("리뷰어 업데이트 테스트")
  public class updateReviewerTest {
    private Member member = MemberStubData.member();

    @Test
    @DisplayName("리뷰어 업데이트 성공 테스트")
    public void updateReviewerSuccessTest() throws Exception {
      // given
      Reviewer reviewer = ReviewerStubData.reviewer(member);
      Reviewer updatingReviewer = ReviewerStubData.updatedReviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.of(reviewer));
      doReturn(updatingReviewer).when(beanUtils).copyNonNullProperties(updatingReviewer, reviewer);
      given(reviewerRepository.save(Mockito.any(Reviewer.class))).willReturn(updatingReviewer);

      // when
      Reviewer updatedReviewer = reviewerService.updateReviewer(updatingReviewer);

      // then
      Assertions.assertThat(updatingReviewer).isEqualTo(updatedReviewer);
    }

    @Test
    @DisplayName("리뷰어 업데이트 실패 테스트 - 리뷰어 찾기 실패")
    public void updateReviewerFailByNotFoundTest() throws Exception {
      // given
      Reviewer updatingReviewer = ReviewerStubData.updatedReviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.updateReviewer(updatingReviewer))
        .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("리뷰어 업데이트 실패 테스트 - 업데이트 권한 없음")
    public void updateReviewerFailByForbiddenTest() throws Exception {
      // given
      Member otherMember = Member.builder()
        .memberId(0L)
        .build();
      Reviewer reviewer = ReviewerStubData.reviewer(otherMember);
      Reviewer updatingReviewer = ReviewerStubData.updatedReviewer(member);
      given(reviewerRepository.findById(Mockito.anyLong())).willReturn(Optional.of(reviewer));

      // when

      // then
      Assertions.assertThatThrownBy(() -> reviewerService.updateReviewer(updatingReviewer))
        .isInstanceOf(BusinessLogicException.class);
    }
  }
}
