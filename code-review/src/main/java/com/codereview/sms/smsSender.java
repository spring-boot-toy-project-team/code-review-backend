package com.codereview.sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class smsSender {

    @Value("${sms.api_key}")
    private String API_KEY;
    @Value("${sms.api_secret}")
    private String API_SECRET;
    @Value("${sms.from}")
    private String FROM;

    public void send(String phone, String smsCode) {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET, "https://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(FROM);
        message.setTo(phone.replaceAll("-", ""));
        message.setText("코드리뷰 커뮤니티 인증 메세지 입니다" + smsCode + " 인증 코드를 입력해주세요");
        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}

