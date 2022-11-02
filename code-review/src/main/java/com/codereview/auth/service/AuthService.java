package com.codereview.auth.service;

import com.codereview.common.dto.token.TokenRequestDto;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, Object> redisTemplate;
  private final HttpServletResponse response;

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
}