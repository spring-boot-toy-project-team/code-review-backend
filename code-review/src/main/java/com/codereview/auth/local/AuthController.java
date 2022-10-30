package com.codereview.auth.local;


import com.codereview.dto.response.MessageResponseDto;
import com.codereview.dto.response.SingleResponseWithMessageDto;
import com.codereview.dto.token.TokenRequestDto;
import com.codereview.dto.token.TokenResponseDto;
import com.codereview.member.dto.MemberRequestDto;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final MemberMapper mapper;
  private final MemberService memberService;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity signUp(@RequestBody @Valid MemberRequestDto.singUpDto signUpDto) {
    signUpDto.setProvider(signUpDto.getProvider());
    signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
    Member member = mapper.signUpDtoToMember(signUpDto);
    memberService.createMember(signUpDto);

    MessageResponseDto message = MessageResponseDto.builder()
      .message("WELCOME")
      .build();

    return new ResponseEntity<>(message, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid MemberRequestDto.loginDto loginDto) {
    TokenResponseDto.Token response = authService.login(mapper.loginDtoToMember(loginDto));
    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(response,
      "SUCCESS"), HttpStatus.OK);
  }

  @PostMapping("/reissue")
  public ResponseEntity reIssue(@RequestBody @Valid TokenRequestDto.ReIssue reIssue) {
    TokenResponseDto.ReIssueToken response =  authService.reIssue(reIssue);
    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(response,
      "SUCCESS"), HttpStatus.OK);
  }
}
