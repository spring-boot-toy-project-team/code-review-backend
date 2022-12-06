package com.codereview.util.email.event;

import com.codereview.util.email.EmailSender;
import com.codereview.member.entity.Member;
import com.codereview.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Configuration
@Component
@Slf4j
public class MemberRegistrationEventListener {
    @Value("${mail.subject.member.registration}")
    private String subject;

    @Value("${mail.redirect-url}")
    private String redirectUrl;

    private final EmailSender emailSender;
    private final MemberService memberService;

    public MemberRegistrationEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    @Async
    @EventListener
    public void listen(MemberRegistrationApplicationEvent event) throws Exception {
        try {
            String[] to = new String[]{event.getMember().getEmail()};
            String message = "<h1>" + event.getMember().getNickName() + "님, 회원 가입이 성공적으로 완료되었습니다.</h1>\n" +
                    "<div>버튼을 누르면 이메일 인증이 완료됩니다.</div>" +
                    "<div>Code Review Community에 오신걸 환영합니다.</div>" +
                    "<div>" +
                    "<a href=\"" + redirectUrl + "/email-validation?email=" + event.getMember().getEmail() + "&code=" + event.getMember().getVerifiedCode() +"\">" +
                    "<'button;\n" +
                    " width: 180px;\n" +
                    " height: 50px;\n" +
                    " font-size: 25px;\n" +
                    " font-family: 'monospace';\n" +
                    " color: #ffffff;\n" +
                    " line-height: 50px;\n" +
                    " text-align: center;\n" +
                    " background-color: #191970;\n" +
                    " border: solid 2px white;\n" +
                    " border-radius: 30px;'>인증 및 홈페이지 이동</button></a></div>";
            emailSender.sendEmail(to, subject, message);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("MailSendException: rollback for Member Registration:");
            Member member = event.getMember();
            memberService.deleteMember(member.getMemberId());
        }
    }
}
