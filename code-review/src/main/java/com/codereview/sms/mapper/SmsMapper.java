package com.codereview.sms.mapper;

import com.codereview.member.entity.Member;
import com.codereview.sms.dto.SmsRequestDto;
import com.codereview.sms.entity.Sms;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsMapper {
    default Sms savePhoneToMember(SmsRequestDto.SavePhoneDto savePhoneDto){
        Sms sms = new Sms();
        Member member = new Member();
        member.setMemberId(savePhoneDto.getMemberId());
        sms.setPhone(savePhoneDto.getPhone());
        sms.setMember(member);
        return sms;
    }
}
