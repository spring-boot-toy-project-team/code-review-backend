package com.codereview.service;

import com.codereview.auth.service.AuthService;
import com.codereview.email.event.MemberRegistrationApplicationEvent;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Spy
    @InjectMocks
    private MemberService memberService;
    @Spy
    @InjectMocks
    private AuthService authService;
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
    @DisplayName("인증 요청 이메일 전송")
    void sendEmail() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("12345678")
                .roles("ROLE_USER")
                .nickName("hgd")
                .build();
        Member save = memberRepository.save(member);
        String email = "hgd@gmail.com";


        //when
        Member findMember = memberService.findMemberByEmail(email);
        System.out.println("findMember = " + findMember.getEmail());
        findMember.setVerifiedCode(UUID.randomUUID().toString());
        Member savedMember = memberRepository.save(findMember);
        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, Mockito.any(Member.class)));

        //then
        assertThat(memberService.findMemberByEmail(findMember.getEmail()).getVerifiedCode())
                .isEqualTo(savedMember.getVerifiedCode());
    }

    @Test
    void verifiedByEmail() {
    }
}
//
//    public void verifiedByEmail(String email, String code){
//        Member findMember = memberService.findMemberByEmail(email);
//        if(!findMember.getVerifiedCode().equals(code)){
//            throw new BusinessLogicException(ExceptionCode.CODE_INCORRECT);
//        }
//        findMember.setEmailVerified(EmailVerified.Y);
//        findMember.setRoles("ROLE_USER");
//        memberRepository.save(findMember);
//    }
//
//    /**
//     * 이메일 인증 요청
//     */
//    public void sendEmail(String email){
//        Member findMember = memberService.findMemberByEmail(email);
//        if(!findMember.getEmail().equals(email)){
//            throw new BusinessLogicException(ExceptionCode.EMAIL_INCORRECT);
//        }
//        findMember.setVerifiedCode(UUID.randomUUID().toString());
//        Member savedMember = memberRepository.save(findMember);
//        publisher.publishEvent(new MemberRegistrationApplicationEvent(this, savedMember));
//    }