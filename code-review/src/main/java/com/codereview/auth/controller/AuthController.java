package com.codereview.auth.controller;


import com.codereview.auth.service.AuthService;
import com.codereview.common.dto.response.MessageResponseDto;
import com.codereview.common.dto.response.SingleResponseDto;
import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.common.dto.token.TokenRequestDto;
import com.codereview.common.dto.token.TokenResponseDto;
import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.service.MemberService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final MemberMapper mapper;
  private final MemberService memberService;
  private final AuthService authService;

  /**
   * 회원 가입 API
   */
  @PostMapping("/signup")
  public ResponseEntity<MessageResponseDto> signUp(@RequestBody @Valid MemberRequestDto.SingUpDto signUpDto) {
    memberService.createMember(mapper.signUpDtoToMember(signUpDto));

    return new ResponseEntity<>(MessageResponseDto.builder()
      .message("WELCOME")
      .build(), HttpStatus.CREATED);
  }

  /**
   * 로그인 API
   */
  @PostMapping("/login")
  public ResponseEntity<MessageResponseDto> login(@RequestBody @Valid MemberRequestDto.LoginDto loginDto) {
    authService.login(mapper.loginDtoToMember(loginDto));

    return new ResponseEntity<>(MessageResponseDto.builder()
      .message("WELCOME")
      .build(), HttpStatus.OK);
  }

  /**
   * 토큰 재발급 API
   */
  @GetMapping("/reissue")
  public ResponseEntity<MessageResponseDto> reIssue(@RequestHeader(name = "Authorization", required = true) String bearerToken,
                                                    @CookieValue(name = "refreshToken", required = false) Cookie cookie) {
    authService.reIssue(bearerToken, cookie);

    return new ResponseEntity<>(MessageResponseDto.builder()
      .message("SUCCESS")
      .build(), HttpStatus.OK);
  }

  /**
   * 이메일 인증 요청
   */
  @GetMapping("/validation")
  public ResponseEntity<MessageResponseDto> validation(@NotBlank @PathParam("email") String email){

    authService.sendEmail(email);

    return new ResponseEntity<>(MessageResponseDto.builder()
            .message("Send Email")
            .build(), HttpStatus.OK);
  }

  /**
   * 이메일 인증
   */
  @GetMapping("/emailValidation")
  public ResponseEntity<MessageResponseDto> emailValidation(@NotBlank @PathParam("email") String email,
                                                            @NotBlank @PathParam("code") String code){

    authService.verifiedEmail(email, code);

    return new ResponseEntity<>(MessageResponseDto.builder()
            .message("SUCCESS")
            .build(), HttpStatus.OK);
  }

  @GetMapping(value = "token")
  public String token(@RequestParam String token, @RequestParam String error) {
    if (StringUtils.isNotBlank(error)) {
      return error;
    } else {
      return token;
    }
  }

}
