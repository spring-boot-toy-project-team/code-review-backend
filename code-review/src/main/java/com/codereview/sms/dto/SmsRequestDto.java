package com.codereview.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

public class SmsRequestDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SavePhoneDto{
        @NotEmpty
        private String phone;

        private long memberId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class validationPhoneDto{
        @NotEmpty
        private String smsCode;

        private long memberId;
    }
}
