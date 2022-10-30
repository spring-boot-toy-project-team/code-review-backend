package com.codereview.auth.local;

import com.codereview.dto.token.TokenRequestDto;
import com.codereview.dto.token.TokenResponseDto;
import com.codereview.exception.BusinessLogicException;
import com.codereview.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, Object> redisTemplate;
  private final HttpServletResponse response;

  public TokenResponseDto.Token login(Member member) {
    Member findMember = findVerifiedMemberByEmail(member.getEmail());

    if(!passwordEncoder.matches(member.getPassword(), findMember.getPassword())) {
      throw new BusinessLogicException(ExceptionCode.PASSWORD_INCORRECT);
    }
    TokenResponseDto.Token token = jwtTokenProvider.createTokenDto(findMember,response);
//    redisTemplate.opsForValue() //tokenProvider에서 저장해야 하는지 서비스단에서 토큰을 주면서 만들어야 하는지 고민입니다
//      .set(findMember.getEmail(), token.getRefreshToken(), token.getRefreshTokenExpiredTime(), TimeUnit.MILLISECONDS);
    return token;
  }

  public String getCurrentMember() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  public TokenResponseDto.ReIssueToken reIssue(TokenRequestDto.ReIssue reIssue) {
    Authentication authentication = jwtTokenProvider.getAuthentication(reIssue.getAccessToken());
    String redisRefreshToken
      = (String) redisTemplate.opsForValue().get(authentication.getName());
    if(StringUtils.hasText(redisRefreshToken)) {
      if(redisRefreshToken.equals(reIssue.getRefreshToken())) // 정상
        return jwtTokenProvider.createReIssueTokenDto(findVerifiedMemberByEmail(authentication.getName()));
      else // refresh 토큰 불일치
        throw new BusinessLogicException(ExceptionCode.TOKEN_IS_INVALID);
    } else { // refresh 토큰 만료
      throw new BusinessLogicException(ExceptionCode.REFRESH_TOKEN_IS_EXPIRED);
    }
  }

  @Transactional(readOnly = true)
  public Member findVerifiedMemberByEmail(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
  }


}
