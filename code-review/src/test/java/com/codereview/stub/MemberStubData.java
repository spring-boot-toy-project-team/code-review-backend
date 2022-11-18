package com.codereview.stub;

import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.entity.AuthProvider;
import com.codereview.member.entity.EmailVerified;
import com.codereview.member.entity.Member;

import java.util.Set;

public class MemberStubData {
  public static Member member() {
    return Member.builder()
      .email("hgd@gmail.com")
      .password("12345678")
      .nickName("hgd")
      .roles("ROLE_USER")
      .memberId(1L)
      .githubUrl("githuburl")
      .phone("010-1234-5678")
      .profileImg("profileImg")
      .provider(AuthProvider.local)
      .skills(Set.of("java", "python"))
      .build();
  }

  public static MemberRequestDto.SingUpDto singUpDto() {
    return MemberRequestDto.SingUpDto.builder()
      .email("hgd@gmail.com")
      .nickName("hgd")
      .password("12345678")
      .build();
  }

  public static MemberRequestDto.LoginDto loginDto() {
    return MemberRequestDto.LoginDto.builder()
      .email("hgd@gmail.com")
      .password("12345678")
      .build();
  }

  public static Member guestMember() {
    return Member.builder()
            .email("hgd@gmail.com")
            .password("12345678")
            .nickName("hgd")
            .roles("ROLE_GUEST")
            .emailVerified(EmailVerified.N)
            .memberId(1L)
            .githubUrl("githuburl")
            .phone("010-1234-5678")
            .profileImg("profileImg")
            .provider(AuthProvider.local)
            .skills(Set.of("java", "python"))
            .build();
  }
}
