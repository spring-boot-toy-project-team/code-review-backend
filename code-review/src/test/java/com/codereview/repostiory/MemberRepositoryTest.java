package com.codereview.repostiory;

import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberRepositoryTest {
  @Autowired
  private MemberRepository memberRepository;

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
}
