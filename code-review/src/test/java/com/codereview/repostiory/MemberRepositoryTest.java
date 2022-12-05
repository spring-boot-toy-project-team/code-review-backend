package com.codereview.repostiory;

import com.codereview.auth.service.AuthService;
import com.codereview.config.TestConfig;
import com.codereview.member.entity.Verified;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;

import com.codereview.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@Import({TestConfig.class})
@DataJpaTest
public class MemberRepositoryTest {
  @Autowired
  private MemberRepository memberRepository;
  @Mock
  private AuthService authService;
  @Mock
  private MemberService memberService;

  @Test
  @DisplayName("회원 정보 저장 테스트")
  void saveMember() throws Exception {
    // given
    Member member = Member.builder()
      .email("hgd@gmail.com")
      .password("12345678")
      .roles("ROLE_USER")
      .nickName("hgd")
      .build();

    // when
    Member savedMember = memberRepository.save(member);

    // then
    assertThat(savedMember).isEqualTo(savedMember);
  }

  @Test
  @DisplayName("회원 정보 조회 테스트")
  void findByEmailTest() throws Exception {
    // given
    String email = "hgd@gmail.com";
    Member member = Member.builder()
      .email(email)
      .password("12345678")
      .roles("ROLE_USER")
      .nickName("hgd")
      .build();
    Member savedMember = memberRepository.save(member);

    // when
    Optional<Member> optionalMember = memberRepository.findByEmail(email);

    // then
    assertThat(optionalMember.isPresent()).isEqualTo(true);
    assertThat(optionalMember.get()).isEqualTo(savedMember);
  }

  @Test
  @DisplayName("이메일 인증 후 db 업데이트")
  void AfterEmailVerified() {
    // given
    String email = "hgd@gmail.com";
    Member member = Member.builder()
            .memberId(1L)
            .email("hgd@gmail.com")
            .password("12345678")
            .roles("ROLE_USER")
            .emailVerified(Verified.Y)
            .nickName("hgd")
            .build();
    Member savedMember = memberRepository.save(member);

    // when
    Optional<Member> optionalMember = memberRepository.findByEmail(email);

    // then
    assertThat(optionalMember.get().getEmailVerified()).isEqualTo(Verified.Y);
    assertThat(optionalMember.get().getRoles()).isEqualTo("ROLE_USER");
  }
}
