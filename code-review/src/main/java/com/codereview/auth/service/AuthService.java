package com.codereview.auth.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.email.event.MemberRegistrationApplicationEvent;
import com.codereview.member.entity.Verified;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.member.service.MemberService;
import com.codereview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, Object> redisTemplate;
  private final HttpServletResponse response;
  private final MemberService memberService;
  private final ApplicationEventPublisher publisher;

  /**
   * 로그인 메서드
   */
  public void login(Member member) {
    Member findMember = findVerifiedMemberByEmail(member.getEmail());

    if(!passwordEncoder.matches(member.getPassword(), findMember.getPassword())) {
      throw new BusinessLogicException(ExceptionCode.MEMBER_INFO_INCORRECT);
    }
    jwtTokenProvider.createTokenDto(findMember,response);
  }

  /**
   * 토큰 재발급 메서드
   */
  public void reIssue(String bearerToken, Cookie refreshCookie) {
    if(refreshCookie == null)
      throw new BusinessLogicException(ExceptionCode.COOKIE_IS_NOT_EXISTS);
    if(bearerToken == null)
      throw new BusinessLogicException(ExceptionCode.AUTHORIZATION_IS_NOT_FOUND);
    String accessToken = jwtTokenProvider.parseToken(bearerToken);
    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    String redisRefreshToken
      = (String) redisTemplate.opsForValue().get(authentication.getName());
    if(StringUtils.hasText(redisRefreshToken)) {
      if(redisRefreshToken.equals(refreshCookie.getValue())) // 정상
        jwtTokenProvider.createTokenDto(findVerifiedMemberByEmail(authentication.getName()), response);
      else // refresh 토큰 불일치
        throw new BusinessLogicException(ExceptionCode.TOKEN_IS_INVALID);
    } else { // refresh 토큰 만료
      throw new BusinessLogicException(ExceptionCode.REFRESH_TOKEN_IS_EXPIRED);
    }
  }

  /**
   * 회원 정보 조회 메서드
   */
  @Transactional(readOnly = true)
  public Member findVerifiedMemberByEmail(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_INFO_INCORRECT));
  }

  /**
   * 이메일 인증
   */
  public void verifiedByCode(String email, String code){
    Member findMember = memberService.findMemberByEmail(email);
    if(!findMember.getVerifiedCode().equals(code)){
      throw new BusinessLogicException(ExceptionCode.CODE_INCORRECT);
    }
    findMember.setEmailVerified(Verified.Y);
    findMember.setRoles("ROLE_USER");
    memberRepository.save(findMember);
  }

  /**
   * 이메일 인증 요청
   */
  public void sendEmail(String email){
    Member findMember = memberService.findMemberByEmail(email);
    if(!findMember.getEmail().equals(email)){
      throw new BusinessLogicException(ExceptionCode.EMAIL_INCORRECT);
    }
    findMember.setVerifiedCode(UUID.randomUUID().toString());
    Member savedMember = memberRepository.save(findMember);
    publisher.publishEvent(new MemberRegistrationApplicationEvent(this, savedMember));
  }
}
