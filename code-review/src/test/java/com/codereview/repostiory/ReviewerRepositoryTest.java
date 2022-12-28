package com.codereview.repostiory;

import com.codereview.config.TestConfig;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.reviewer.entity.Career;
import com.codereview.reviewer.entity.Position;
import com.codereview.reviewer.entity.Reviewer;
import com.codereview.reviewer.repository.ReviewerRepository;
import com.codereview.stub.MemberStubData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Import({TestConfig.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReviewerRepositoryTest {
  @Autowired
  private ReviewerRepository reviewerRepository;
  @Autowired
  private MemberRepository memberRepository;
  private Member member;

  @BeforeEach
  public void beforeEach() {
    Member memberStub = MemberStubData.member();
    member = memberRepository.save(memberStub);
  }

  @Test
  @DisplayName("리뷰어 등록 테스트")
  public void saveReviewerTest() throws Exception {
    // given
    Reviewer reviewer = Reviewer.builder()
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .member(member)
      .career(Career.JUNIOR)
      .position(Position.BACK_END.name())
      .build();

    // when
    Reviewer savedReviewer = reviewerRepository.save(reviewer);

    // then
    Assertions.assertThat(reviewer).isEqualTo(savedReviewer);
  }

  @Test
  @DisplayName("리뷰어 조회 테스트")
  public void getReviewerAllTest() throws Exception {
    // given
    Reviewer reviewer = Reviewer.builder()
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .member(member)
      .career(Career.JUNIOR)
      .position(Position.BACK_END.name())
      .build();

    reviewerRepository.save(reviewer);

    // when
    List<Reviewer> reviewerList = reviewerRepository.findAll();

    // then
    Assertions.assertThat(1).isEqualTo(reviewerList.size());
  }

  @Test
  @DisplayName("리뷰어 삭제 테스트")
  public void deleteReviewerTest() throws Exception {
    // given
    Reviewer reviewer = Reviewer.builder()
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .member(member)
      .career(Career.JUNIOR)
      .position(Position.BACK_END.name())
      .build();

    Reviewer savedReviewer = reviewerRepository.save(reviewer);

    // when
    reviewerRepository.delete(savedReviewer);

    // then
    Optional<Reviewer> optionalReviewer = reviewerRepository.findById(savedReviewer.getReviewerId());
    Assertions.assertThat(false).isEqualTo(optionalReviewer.isPresent());
  }

  @Test
  @DisplayName("회원 식별자로 리뷰어 찾기 테스트")
  public void findByMemberIdTest() throws Exception {
    // given
    Reviewer reviewer = Reviewer.builder()
      .introduction("hi")
      .skills(Set.of("Spring", "React"))
      .member(member)
      .career(Career.JUNIOR)
      .position(Position.BACK_END.name())
      .build();

    Reviewer savedReviewer = reviewerRepository.save(reviewer);

    // when
    Optional<Reviewer> optionalReviewer = reviewerRepository.findByMemberId(member.getMemberId());

    // then
    Assertions.assertThat(true).isEqualTo(optionalReviewer.isPresent());
  }

  @Nested
  public class getReviewerTest {
    private Reviewer savedReviewer;
    @BeforeEach
    public void beforeEach() throws Exception {
      Reviewer reviewer = Reviewer.builder()
        .introduction("hi")
        .skills(Set.of("Spring", "React"))
        .member(member)
        .career(Career.JUNIOR)
        .position(Position.BACK_END.name())
        .build();

      savedReviewer = reviewerRepository.save(reviewer);
    }

    @Test
    @DisplayName("리뷰어 식별자로 리뷰어 찾기 성공 테스트")
    public void getReviewerSuccessTest() throws Exception {
      // given
      Long reviewerId = savedReviewer.getReviewerId();

      // when
      Optional<Reviewer> optionalReviewer = reviewerRepository.findById(reviewerId);

      // then
      Assertions.assertThat(true).isEqualTo(optionalReviewer.isPresent());
      Assertions.assertThat(savedReviewer).isEqualTo(optionalReviewer.get());
    }

    @Test
    @DisplayName("리뷰어 식별자로 리뷰어 찾기 실패 테스트")
    public void getReviewerFailTest() throws Exception {
      Long reviewerId = 0L;

      // when
      Optional<Reviewer> optionalReviewer = reviewerRepository.findById(reviewerId);

      // then
      Assertions.assertThat(false).isEqualTo(optionalReviewer.isPresent());
    }
  }

}
