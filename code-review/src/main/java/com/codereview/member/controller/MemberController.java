package com.codereview.member.controller;

import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.member.entity.Member;
import com.codereview.member.mapper.MemberMapper;
import com.codereview.member.service.MemberService;
import com.codereview.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@AllArgsConstructor
public class MemberController {
  private final MemberService memberService;
  private final MemberMapper mapper;

  @GetMapping("/info")
  public ResponseEntity getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    Member member = memberService.findMember(customUserDetails.getMember().getMemberId());

    return new ResponseEntity<>(new SingleResponseWithMessageDto<>(mapper.memberToMemberInfo(member),
      "SUCCESS"),
      HttpStatus.OK);
  }
}
