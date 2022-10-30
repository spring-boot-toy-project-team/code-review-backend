package main.wheelmaster.Auth.local;


import lombok.RequiredArgsConstructor;
import main.wheelmaster.member.dto.MemberRequestDto;
import main.wheelmaster.member.entity.Member;
import main.wheelmaster.member.mapper.MemberMapper;
import main.wheelmaster.member.service.MemberService;
import main.wheelmaster.response.MessageResponseDto;
import main.wheelmaster.response.SingleResponseWithMessageDto;
import main.wheelmaster.response.token.TokenRequestDto;
import main.wheelmaster.response.token.TokenResponseDto;
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
