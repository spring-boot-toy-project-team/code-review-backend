package com.codereview.member.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.AuthProvider;
import com.codereview.member.entity.Verified;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.repository.MemberRepository;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final CustomBeanUtils<Member> beanUtils;

  public Member createMember(Member member) {
    verifyEmail(member.getEmail());
    member.setPassword(passwordEncoder.encode(member.getPassword()));
    member.setRoles("ROLE_GUEST");
    member.setProvider(AuthProvider.local);
    member.setEmailVerified(Verified.N);
    return memberRepository.save(member);
  }

  @CachePut(key = "#member.email", value = "loadUserByUsername")
  public Member updateMember(Member member) {
    Member findMember = findVerifiedMember(member.getMemberId());
    Member updatedMember = beanUtils.copyNonNullProperties(member, findMember);

    return memberRepository.save(updatedMember);
  }

  @CacheEvict(key = "#root.target.findById(#memberId).email", value = "loadUserByUsername")
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
