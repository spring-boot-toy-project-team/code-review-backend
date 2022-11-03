package com.codereview.security.jwt;

import com.codereview.common.dto.token.TokenResponseDto;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  @Value("spring.jwt.secret")
  private String secretKey;
  private static final String GRANT_TYPE = "Bearer ";
  private static final Long ACCESS_TOKEN_EXPIRED_IN = Duration.ofMinutes(30).toMillis(); //만료시간 30분
  private static final Long REFRESH_TOKEN_EXPIRED_IN = Duration.ofDays(1).toMillis();    //만료시간 1일
  private static final String REFRESH_TOKEN_KEY = "refreshToken";
  private static final String ROLES = "roles";
  private final UserDetailsService userDetailsService;
  private final RedisTemplate<String, Object> redisTemplate;

  @PostConstruct
  protected void init(){
    secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 토큰 생성 메서드
   * accessToekn -> header, refreshToken -> cookie 저장
   */
  public void createTokenDto(Member member, HttpServletResponse response){
    Claims claims = Jwts.claims().setSubject(member.getEmail());
    claims.put(ROLES, member.getRoleList());

    Date now = new Date();

    String accessToken = Jwts.builder()
      .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_IN))
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();

    String refreshToken = Jwts.builder()
      .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_IN))
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();

    redisTemplate.opsForValue().set(member.getEmail(), refreshToken,REFRESH_TOKEN_EXPIRED_IN, TimeUnit.MILLISECONDS);
    redisTemplate.expire(member.getEmail(), REFRESH_TOKEN_EXPIRED_IN,TimeUnit.MILLISECONDS);

    ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_KEY, refreshToken)
      .path("/")
      .sameSite("Lax")
      .httpOnly(true)
      .secure(true)
      .maxAge(REFRESH_TOKEN_EXPIRED_IN)
      .build();

    response.addHeader("Authorization", GRANT_TYPE + accessToken);
    response.addHeader("Set-Cookie",responseCookie.toString());

  }

  /**
   * 토큰 이용 인증 정보 조회
   */
  public Authentication getAuthentication(String token) {
    Claims claims = parseClaims(token);

    if(claims.get(ROLES) == null)
      throw new BusinessLogicException(ExceptionCode.ROLE_IS_NOT_EXISTS);
    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /**
   * 토큰 parse
   */
  public String parseToken(String header) {
    if (StringUtils.hasText(header) && header.startsWith(GRANT_TYPE)) {
      return header.substring(7);
    }
    return null;
  }

  /**
   * 토큰 복호화
   */
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  /**
   * 토큰 유효성 검사
   */
  public boolean validationToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.error("잘못된 Jwt 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.error("만료된 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("지원하지 않는 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.error("잘못된 토큰입니다.");
    }
    return false;
  }

}
