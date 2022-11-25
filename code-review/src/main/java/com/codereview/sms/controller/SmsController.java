package com.codereview.sms.controller;

import com.codereview.common.dto.response.MessageResponseDto;
import com.codereview.security.CustomUserDetails;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.mapper.SmsMapper;
import com.codereview.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;
    private final SmsMapper mapper;

    /**
     * 핸드폰 번호 등록
     */
    @PostMapping("/phone")
    public ResponseEntity<MessageResponseDto> savePhone(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @RequestBody @Valid SmsRequestDto.SavePhoneDto savePhoneDto){
        savePhoneDto.setMemberId(customUserDetails.getMember().getMemberId());
        smsService.saveMemberPhone(mapper.savePhoneToMember(savePhoneDto));

        return new ResponseEntity<>(MessageResponseDto.builder()
                .message("OK")
                .build(), HttpStatus.CREATED);
    }

    /**
     * 핸드폰 번호 인증
     */
    @PostMapping("/phone-validation")
    public ResponseEntity<MessageResponseDto> validation(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @RequestBody @Valid SmsRequestDto.validationPhoneDto validationPhoneDto){
        validationPhoneDto.setMemberId(customUserDetails.getMember().getMemberId());

        smsService.verifiedBySmsCode(validationPhoneDto.getMemberId(), validationPhoneDto.getSmsCode());
        System.out.println(validationPhoneDto.getMemberId());
        return new ResponseEntity<>(MessageResponseDto.builder()
                .message("SUCCESS")
                .build(), HttpStatus.OK);
    }
}
