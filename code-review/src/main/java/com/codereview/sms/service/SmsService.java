package com.codereview.sms.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.sms.SMS;
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
    private final SMS Sms;

    public Sms saveMemberPhone(Sms sms) {
        verifyPhone(sms.getPhone());
        sms.setPhone(sms.getPhone());
        sms.setSmsCode(generateAuthNo());
        sms.setSendAt(LocalDateTime.now());
        sms.setMember(sms.getMember());
        Sms.send(sms.getPhone(),sms.getSmsCode());
        return smsRepository.save(sms);
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
