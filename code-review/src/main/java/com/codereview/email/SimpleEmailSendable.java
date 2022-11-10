package com.codereview.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
public class SimpleEmailSendable implements EmailSendable {
    private final JavaMailSender javaMailSender;

    public SimpleEmailSendable(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(String[] to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setText(message);
        mailMessage.setSubject(subject);
        mailMessage.setFrom("Code Review" + mailMessage.getFrom());
        javaMailSender.send(mailMessage);
    }
}
