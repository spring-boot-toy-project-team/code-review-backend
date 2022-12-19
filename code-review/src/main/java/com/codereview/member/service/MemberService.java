package com.codereview.member.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.AuthProvider;
import com.codereview.member.entity.Verified;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.repository.MemberRepository;
import com.codereview.util.email.event.MemberRegistrationApplicationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher publisher;


  public Member createMember(Member member) {
    verifyEmail(member.getEmail());
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    member.setRoles("ROLE_GUEST");
    member.setProvider(AuthProvider.local);
    member.setEmailVerified(Verified.N);
    member.setVerifiedCode(UUID.randomUUID().toString());
    publisher.publishEvent(new MemberRegistrationApplicationEvent(this, member));
    return memberRepository.save(member);
  }

  public Member updateMember(Member member) {

    Member findMember = findVerifiedMember(member.getMemberId());

    Optional.ofNullable(member.getNickName()).ifPresent(findMember::setNickName);
    Optional.ofNullable(member.getPassword()).ifPresent(findMember::setPassword);
    Optional.ofNullable(member.getPhone()).ifPresent(findMember::setPhone);
    Optional.ofNullable(member.getGithubUrl()).ifPresent(findMember::setGithubUrl);
    Optional.ofNullable(member.getProfileImg()).ifPresent(findMember::setProfileImg);
    Optional.ofNullable(member.getSkills()).ifPresent(findMember::setSkills);
    return memberRepository.save(findMember);
  }

  public void deleteMember(long memberId) {
    Member member = findVerifiedMember(memberId);
    memberRepository.delete(member);
  }

  @Transactional(readOnly = true)
  public void verifyEmail(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    if(optionalMember.isPresent())
      throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
  }

  @Transactional(readOnly = true)
  public Member findMember(long memberId) {
    return findVerifiedMember(memberId);
  }

  @Transactional(readOnly = true)
  private Member findVerifiedMember(long memberId) {
    return memberRepository.findById(memberId)
      .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
  }
  @Transactional(readOnly = true)
  public Member findMemberByEmail(String email){
    return findVerifiedMemberByEmail(email);
  }

  @Transactional(readOnly = true)
  private Member findVerifiedMemberByEmail(String email) {
    return memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
  }
}
