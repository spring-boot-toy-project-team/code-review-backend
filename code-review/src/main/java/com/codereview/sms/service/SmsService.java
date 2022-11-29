package com.codereview.sms.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.sms.smsSender;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
    private final SmsRepository smsRepository;
    private final smsSender smsSender;
    private final MemberRepository memberRepository;

    public Sms saveMemberPhone(Sms sms) {
        sms.setSmsCode(generateAuthNo());
        sms.setMember(sms.getMember());
        smsSender.send(sms.getPhone(),sms.getSmsCode());
        return smsRepository.save(sms);
    }

    /**
     *
     * 핸드폰 번호로 발송된 인증번호 확인
     */
    public void verifiedBySmsCode(String smsCode, long memberId){
        Sms findPhone = findSmsCode(smsCode, memberId);
        if(!findPhone.getSmsCode().equals(smsCode)){
            throw new BusinessLogicException(ExceptionCode.CODE_INCORRECT);
        }
        Member member = findVerifiedMember(memberId);
        member.setPhone(findPhone.getPhone());
        memberRepository.save(member);
    }

    /**
     *
     * 입력된 smsCode로 DB에서 찾아서 조회
     */
    @Transactional(readOnly = true)
    public Sms findSmsCode(String smsCode, long memberId){
        return findVerifiedSmsCode(smsCode, memberId);
    }

    @Transactional(readOnly = true)
    public Sms findVerifiedSmsCode(String smsCode, long memberId){
        return smsRepository.findBySmsCode(smsCode, memberId)
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.CODE_NOT_FOUND));

    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    @Transactional(readOnly = true)
    private Member findVerifiedMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    /**
     * 인증코드 만들어주는 로직
     */
    public static String generateAuthNo() {
        return RandomStringUtils.randomNumeric(6);
    }
}
