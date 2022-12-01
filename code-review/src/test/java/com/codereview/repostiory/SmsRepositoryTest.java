package com.codereview.repostiory;

import com.codereview.config.TestConfig;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.sms.entity.Sms;
import com.codereview.sms.repository.SmsRepository;
import com.codereview.stub.MemberStubData;
import com.codereview.util.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestConfig.class})
@DataJpaTest
public class SmsRepositoryTest {

  @Autowired
  private SmsRepository smsRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Member member;
  @BeforeEach
  void beforeEach() {
    member = memberRepository.save(MemberStubData.member());
  }


  @Test
  @DisplayName("핸드폰 번호 저장 테스트")
  void savePhoneTest(){
    //given
    String phone = "010-1234-5678";
    Sms sms = Sms.builder()
            .member(member)
            .phone(phone)
            .smsId(1L)
            .smsCode(RandomStringUtils.generateAuthNo())
            .build();
    //when
    Sms savedSms = smsRepository.save(sms);

    //then
    assertThat(sms.getSmsCode()).isEqualTo(savedSms.getSmsCode());
    assertThat(sms.getMember().getMemberId()).isEqualTo(savedSms.getMember().getMemberId());
  }
}
