package com.codereview.sms.mapper;

import com.codereview.member.entity.Member;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsMapper {
    default Sms savePhoneToSms(SmsRequestDto.SavePhoneDto savePhoneDto){
        return Sms.builder()
            .member(Member.builder().memberId(savePhoneDto.getMemberId()).build())
            .phone(savePhoneDto.getPhone())
            .build();
    }
}
