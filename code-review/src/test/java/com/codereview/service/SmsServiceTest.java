package com.codereview.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.repository.SmsRepository;
import com.codereview.sms.service.SmsService;
import com.codereview.sms.smsSender;
import com.codereview.stub.SmsStubData;
import com.codereview.util.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

  @Spy
  @InjectMocks
  private SmsService smsService;

  @Mock
  private SmsRepository smsRepository;

  @Mock
  private smsSender smsSender;


  @Test
  @DisplayName("핸드폰 인증 요청 성공")
  void SuccessSendPhone(){
    //given
    Sms sms = SmsStubData.sms();
    String smsCode = sms.getSmsCode();

    given(smsRepository.save(Mockito.any())).willReturn(sms);
    smsSender.send(sms.getPhone(),sms.getSmsCode());

    //when
    Sms savePhone = smsService.saveMemberPhone(sms);

    //then
    assertThat(sms.getPhone()).isEqualTo(savePhone.getPhone());
    assertThat(sms.getMember().getMemberId()).isEqualTo(savePhone.getMember().getMemberId());
  }

  @Test
  @DisplayName("핸드폰 인증 성공")
  void SuccessVerifiedByPhone(){
    //given
    Sms sms = SmsStubData.sms();
    String smsCode = RandomStringUtils.generateAuthNo();

    given(smsRepository.save(Mockito.any())).willReturn(sms);
    lenient().doReturn(sms).when(smsService).findSmsCode(smsCode, sms.getMember().getMemberId());

    //when
    Sms savePhone = smsService.saveMemberPhone(sms);

    //then
    assertThat(sms.getPhone()).isEqualTo(savePhone.getPhone());
    assertThat(sms.getMember().getMemberId()).isEqualTo(savePhone.getMember().getMemberId());

  }

  @Test
  @DisplayName("핸드폰 인증 실패")
  void FailureVerifiedByPhone(){
    //given
    Sms sms = SmsStubData.sms();
    String smsCode = RandomStringUtils.generateAuthNo();

    //when

    //then
    lenient().doThrow(new BusinessLogicException(ExceptionCode.CODE_NOT_FOUND))
            .when(smsService).verifiedBySmsCode(smsCode, sms.getMember().getMemberId());

  }
}
