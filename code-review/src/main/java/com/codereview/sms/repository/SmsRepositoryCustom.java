package com.codereview.sms.repository;

import com.codereview.sms.entity.Sms;

import java.util.Optional;

public interface SmsRepositoryCustom {
    Optional<Sms> findByPhone(String phone);

    Optional<Sms> findBySmsCode(String smsCode, long memberId);
}
