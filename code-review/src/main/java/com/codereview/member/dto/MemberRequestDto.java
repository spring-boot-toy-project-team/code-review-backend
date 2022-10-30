package com.codereview.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class MemberRequestDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class singUpDto{
        @NotBlank
        @Email
        @Pattern(regexp = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$", message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank(message = "최소 10자리 이상이어야 합니다")
        @Length(min = 10)
        private String password;

        @NotBlank
        private String nickName;

//        @NotBlank(message = "'-'을 사용하여 휴대폰번호를 정확하게 입력해 주세요")
//        @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
//        private String phone;

        private String provider;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class loginDto {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$")
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class updateDto{
        @Length(min = 10)
        private String password;
        private String nickName;
        private String phone;
        private String profileImg;
        private String githubUrl;
        private Set<String> skills;

        private long memberId;

        public updateDto setMemberId(long memberId){
            this.memberId = memberId;
            return this;
        }
    }
}