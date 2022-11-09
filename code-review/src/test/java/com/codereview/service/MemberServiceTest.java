package com.codereview.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.member.service.MemberService;
import com.codereview.stub.MemberStubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
  @Spy
  @InjectMocks
  private MemberService memberService;
  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원 이메일 유효성 검사 테스트")
  void verifyEmailTest() throws Exception {
    // given
    String email = "hgd@gmail.com";

    // when

    // then
    lenient().doThrow(new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS))
      .when(memberService)
      .verifyEmail(email);
  }


  @Test
  @DisplayName("회원 정보 저장 테스트")
  void createMemberTest() throws Exception {
    // given
    Member member = MemberStubData.member();
    lenient().doNothing()
      .when(memberService)
      .verifyEmail(Mockito.anyString());
    when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);

    // when
    Member savedMember = memberService.createMember(member);

    // then
    assertThat(savedMember).isEqualTo(member);
    verify(memberRepository, times(1)).save(Mockito.any(Member.class));
  }
}
