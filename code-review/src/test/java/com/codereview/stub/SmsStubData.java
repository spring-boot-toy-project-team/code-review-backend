package com.codereview.stub;

import com.codereview.member.entity.Member;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import com.codereview.util.RandomStringUtils;

public class SmsStubData {
    private final static Member member = MemberStubData.member();
    private final static String code = RandomStringUtils.generateAuthNo();

    public static Sms sms() {
        return Sms.builder()
            .smsId(1L)
            .phone("010-1234-5678")
            .member(member)
            .build();
    }

    public static SmsRequestDto.SavePhoneDto savePhoneDto(){
        return SmsRequestDto.SavePhoneDto.builder()
            .memberId(1L)
            .phone("010-1234-5678")
            .build();
    }

    public static Sms SavePhoneDtoToSmsDto(SmsRequestDto.SavePhoneDto savePhoneDto){
        return Sms.builder()
            .member(member)
            .phone(savePhoneDto.getPhone())
            .smsId(1L)
            .build();
    }

    public static SmsRequestDto.validationPhoneDto validationPhoneDto(){
        return SmsRequestDto.validationPhoneDto.builder()
            .memberId(member.getMemberId())
            .smsCode(code)
            .build();
    }
}
