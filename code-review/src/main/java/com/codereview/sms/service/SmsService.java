package com.codereview.sms.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.Verified;
import com.codereview.member.repository.MemberRepository;
import com.codereview.sms.smsSender;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
    private final SmsRepository smsRepository;
    private final smsSender smsSender;
    private final MemberRepository memberRepository;

    public Sms saveMemberPhone(Sms sms) {
        verifyPhone(sms.getPhone());
        sms.setPhone(sms.getPhone());
        sms.setSmsCode(generateAuthNo());
        sms.setSendAt(LocalDateTime.now());
        sms.setSmsVerified(Verified.N);
//        smsSender.send(sms.getPhone(),sms.getSmsCode());
        return smsRepository.save(sms);
    }

    /**
     *
     * 핸드폰 번호로 발송된 인증번호 확인
     */
    public void verifiedBySmsCode(Sms sms, String smsCode){
        Sms findPhone = findMemberByPhone(sms.getPhone());
        if(!findPhone.getSmsCode().equals(smsCode)){
            throw new BusinessLogicException(ExceptionCode.CODE_INCORRECT);
        }
        findPhone.setSmsVerified(Verified.Y);
        smsRepository.save(findPhone);
    }


    @Transactional(readOnly = true)
    public Sms findMemberByPhone(String phone){
        return findVerifiedMemberByPhone(phone);
    }

    @Transactional(readOnly = true)
    private Sms findVerifiedMemberByPhone(String phone) {
        return smsRepository.findByPhone(phone)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PHONE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public void verifyPhone(String phone){
        Optional<Sms> optionalSms = smsRepository.findByPhone(phone);
        if (optionalSms.isPresent())
            throw new BusinessLogicException(ExceptionCode.PHONE_ALREADY_EXISTS);
    }

    public static String generateAuthNo() {
        return RandomStringUtils.randomNumeric(6);
    }
}
