package com.codereview.sms.repository;

import com.codereview.sms.entity.Sms;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.codereview.sms.entity.QSms.sms;

@Repository
@RequiredArgsConstructor
public class SmsRepositoryImpl implements SmsRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Sms> findByPhone(String phone) {
        return Optional.empty();
    }

    @Override
    public Optional<Sms> findBySmsCode(String smsCode, long memberId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(sms)
                .from(sms)
                .where(sms.smsCode.eq(smsCode), sms.member.memberId.eq(memberId))
                .orderBy(sms.smsCode.desc())
                .limit(1)
                .fetchOne());
    }
}
