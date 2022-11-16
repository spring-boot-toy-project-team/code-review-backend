package com.codereview.service;

import com.codereview.auth.service.AuthService;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.email.event.MemberRegistrationApplicationEvent;
import com.codereview.member.entity.EmailVerified;
import com.codereview.member.entity.Member;
import com.codereview.member.repository.MemberRepository;
import com.codereview.member.service.MemberService;
import com.codereview.stub.MemberStubData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Spy
    @InjectMocks
    private AuthService authService;
    @Mock
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ApplicationEventPublisher publisher;


//    @BeforeEach
//    void member(){
//        Member member = Member.builder()
//                .memberId(1L)
//                .email("hgd@gmail.com")
//                .password("12345678966")
//                .roles("ROLE_GUEST")
//                .nickName("hgd")
//                .build();
//        Member save = memberRepository.save(member);
//        System.out.println("member = " + member.getMemberId()+" "+ member.getNickName()+ " " + member.getEmail());
//    }

    @Test
    @DisplayName("인증 요청 이메일 전송 성공")
    void SuccessSendEmail() {
        // TODO: 성공 & 실패에 따른 테스트 케이스를 각각 만들어야 할것 같습니다.
        //given
        Member member = MemberStubData.member();
        String email = member.getEmail();

        given(memberRepository.save(Mockito.any())).willReturn(member);
        doReturn(member).when(memberService).findMemberByEmail(email);

        //when
        Member findMember = memberService.findMemberByEmail(email);
        findMember.setVerifiedCode(UUID.randomUUID().toString());
        Member savedMember = memberRepository.save(findMember);
        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, Mockito.any(Member.class)));

        //then
        assertThat(memberService.findMemberByEmail(findMember.getEmail()).getVerifiedCode())
                .isEqualTo(savedMember.getVerifiedCode());
    }

    @Test
    @DisplayName("인증 요청 이메일 전송 실패")
    void FailureSendEmail(){
        //given
        Member member = MemberStubData.member();
        String email = "hgd@naver.com";

        doReturn(member).when(memberService).findMemberByEmail(email);

        //when
        Member findMember = memberService.findMemberByEmail(email);

        //then
        lenient().doThrow(new BusinessLogicException(ExceptionCode.EMAIL_INCORRECT))
                .when(memberService).findMemberByEmail(email);
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void SuccessVerifiedByCode() {
        //given
        Member member = MemberStubData.guestMember();
        String email = member.getEmail();
        String code = UUID.randomUUID().toString();

        given(memberRepository.save(Mockito.any())).willReturn(member);
        doReturn(member).when(memberService).findMemberByEmail(email);

        //when
        Member findMember = memberService.findMemberByEmail(email);
        findMember.setVerifiedCode(UUID.randomUUID().toString());
        Member savedMember = memberRepository.save(findMember);
        lenient().doNothing().when(authService).verifiedByCode(Mockito.anyString(), Mockito.anyString());
        savedMember.setEmailVerified(EmailVerified.Y);
        savedMember.setRoles("ROLE_USER");
        memberRepository.save(savedMember);


        //then
        assertThat(memberService.findMemberByEmail(findMember.getEmail()).getVerifiedCode())
                .isEqualTo(savedMember.getVerifiedCode());
        assertThat(memberService.findMemberByEmail(member.getEmail()).getEmail()).isEqualTo(findMember.getEmail());
        assertThat(memberService.findMemberByEmail(savedMember.getEmail()).getEmailVerified()).isEqualTo(EmailVerified.Y);
        assertThat(memberService.findMemberByEmail(savedMember.getEmail()).getRoles()).isEqualTo("ROLE_USER");



    }

    @Test
    @DisplayName("이메일 인증 실패")
    void FailureVerifiedByCode() {
        //given
        Member member = MemberStubData.guestMember();
        String email = member.getEmail();
        String code = UUID.randomUUID().toString();

        //when
        lenient().doNothing().when(authService).verifiedByCode(Mockito.anyString(),Mockito.anyString());

        //then
        lenient().doThrow(new BusinessLogicException(ExceptionCode.CODE_INCORRECT))
                .when(authService).verifiedByCode(email,code);
    }
}
