package com.codereview.security;

import com.codereview.dto.token.TokenResponseDto;
import com.codereview.exception.BusinessLogicException;
import com.codereview.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private static final String grantType = "Bearer";
    private static final Long ACCESS_TOKEN_EXPIRED_IN = Duration.ofMinutes(30).toMillis(); //만료시간 30분
    private static final Long REFRESH_TOKEN_EXPIRED_IN = Duration.ofDays(1).toMillis();    //만료시간 1일

    private static final String ROLES = "roles";
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    protected void init(){
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponseDto.Token createTokenDto(Member member, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put(ROLES, member.getRoleList());

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) //헤더 타입 지정. JWT를 사용하기에 Header.JWT_TYPE으로 명시, setHeaderParam을 호출하면 key-value 쌍으로 header에 추가
                .setClaims(claims) //payload에는 토큰에 담을 정보
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_IN))
                .signWith(SignatureAlgorithm.HS256, secretKey) //해싱 알고리즘과 시크릿 키 설정
                .compact();

        String refreshToken = Jwts.builder()
//                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_IN))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        redisTemplate.opsForValue().set(member.getEmail(), refreshToken,REFRESH_TOKEN_EXPIRED_IN, TimeUnit.MILLISECONDS); //redis에 refresh token 저장
//        redisTemplate.expire(member.getEmail(), REFRESH_TOKEN_EXPIRED_IN,TimeUnit.MILLISECONDS); //redis에 refresh token 만료

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/") //다른 엔드포인트로 가도 쿠키를 가지고 다닐수 있도록 "/" 지정
                .sameSite("Lax") //동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능 //"Lax" 랑 차이가 뭘까 Lax 설정에서도 문제 없게끔 쿠키에 대한 의존성을 낮추는 것이 권장 되지만 바로 수정개발이 힘든 경우는 쿠키의 SameSite설정을 기존의 기본값이었던 None으로 설정하여 임시로 해결
                .httpOnly(true) //프론트엔드에서 쿠키에 접근하려면 false로 해야한다고 하는데 지금은 접근할 일이 없을거라 생각하고 true
                .secure(true) //https끼리 쿠키를 요청하겠다
                .maxAge(REFRESH_TOKEN_EXPIRED_IN)
                .build();

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie",responseCookie.toString());

        return TokenResponseDto.Token.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpiredTime(ACCESS_TOKEN_EXPIRED_IN)
                .refreshToken(refreshToken)
                .refreshTokenExpiredTime(REFRESH_TOKEN_EXPIRED_IN)
                .build();
    }

    public TokenResponseDto.ReIssueToken createReIssueTokenDto(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put(ROLES, member.getRoles());

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims) //정보
                .setIssuedAt(now)  //토큰 발행 시간
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_IN)) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenResponseDto.ReIssueToken.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpiredTime(ACCESS_TOKEN_EXPIRED_IN)
                .build();
    }

    // jwt로 인증정보를 조회
    public Authentication getAuthentication(String token) {
        // claims 추출
        Claims claims = parseClaims(token);
        // 권한 정보 없으면
        if(claims.get(ROLES) == null)
            throw new BusinessLogicException(ExceptionCode.ROLE_IS_NOT_EXISTS);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
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
